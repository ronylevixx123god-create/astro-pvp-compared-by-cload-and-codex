package com.decimatemc.blackmarketauction;

import me.recolance.auction.data.DataHolder;
import net.astropvp.auction.Auction;
import net.astropvp.auction.AuctionManager;
import net.astropvp.util.CustomConfigFile;
import net.astropvp.util.Utils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.*;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Tests actual recovered lifecycle and bidding code against Bukkit/Vault boundaries. */
public class RecoveryTest {
    private BlackMarketAuction plugin;
    private AuctionManager manager;
    private Economy economy;
    private BukkitScheduler scheduler;
    private MockedStatic<Bukkit> bukkit;
    private YamlConfiguration config;
    private ItemStack item;
    private Player bidder;
    private EconomyResponse success;
    private EconomyResponse failure;
    private CustomConfigFile history;

    @Before
    public void setup() throws Exception {
        plugin = mock(BlackMarketAuction.class);
        economy = mock(Economy.class);
        scheduler = mock(BukkitScheduler.class);
        config = new YamlConfiguration();
        history = mock(CustomConfigFile.class);
        when(history.getConfig()).thenReturn(new YamlConfiguration());
        when(plugin.getConfig()).thenReturn(config);
        when(plugin.getHistory()).thenReturn(history);
        when(plugin.getEconomy()).thenReturn(economy);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("BAH-test"));
        singleton(plugin);
        bukkit = mockStatic(Bukkit.class);
        bukkit.when(Bukkit::getScheduler).thenReturn(scheduler);
        manager = new AuctionManager();
        // Original bidding tests mock external durability just as they mock Vault/SQL.
        Field journal = AuctionManager.class.getDeclaredField("journal");
        journal.setAccessible(true);
        journal.set(manager, mock(net.astropvp.auction.AuctionJournal.class));
        when(plugin.getAuctionManager()).thenReturn(manager);
        item = mock(ItemStack.class);
        ItemMeta meta = mock(ItemMeta.class);
        when(item.getItemMeta()).thenReturn(meta);
        when(item.getType()).thenReturn(Material.DIAMOND);
        when(item.clone()).thenReturn(item);
        when(meta.getDisplayName()).thenReturn("Recovered reward");
        bidder = mock(Player.class);
        when(bidder.getUniqueId()).thenReturn(UUID.randomUUID());
        when(bidder.getName()).thenReturn("NewBidder");
        success = new EconomyResponse(100000, 0, EconomyResponse.ResponseType.SUCCESS, "");
        failure = new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "test failure");
        when(economy.has(any(OfflinePlayer.class), anyDouble())).thenReturn(true);
        when(economy.withdrawPlayer(any(OfflinePlayer.class), anyDouble())).thenReturn(success);
        when(economy.depositPlayer(any(OfflinePlayer.class), anyDouble())).thenReturn(success);
    }

    @After
    public void cleanup() throws Exception {
        bukkit.close();
        singleton(null);
        Field field = AuctionManager.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
    }

    private void singleton(BlackMarketAuction value) throws Exception {
        Field field = BlackMarketAuction.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, value);
    }

    private Auction auction() {
        Auction auction = new Auction(item, 100000);
        manager.setCurrentAuction(auction);
        return auction;
    }

    @Test
    public void bidChargesNewBidderAndRefundsOfflinePreviousBidder() {
        Auction auction = auction();
        UUID oldId = UUID.randomUUID();
        OfflinePlayer previous = mock(OfflinePlayer.class);
        bukkit.when(() -> Bukkit.getOfflinePlayer(oldId)).thenReturn(previous);
        auction.currentTopBidder = oldId;
        auction.currentBid = 100000;
        auction.doBid(bidder, 200000);
        verify(economy).withdrawPlayer(bidder, 200000);
        verify(economy).depositPlayer(previous, 100000);
        verify(economy, never()).withdrawPlayer(previous, 100000);
        assertEquals(bidder.getUniqueId(), auction.currentTopBidder);
        assertEquals(200000, auction.currentBid, 0);
    }

    @Test
    public void failedWithdrawalDoesNotChangeAuction() {
        Auction auction = auction();
        when(economy.withdrawPlayer(bidder, 100000)).thenReturn(failure);
        auction.doBid(bidder, 100000);
        assertNull(auction.currentTopBidder);
        assertEquals(0, auction.currentBid, 0);
    }

    @Test
    public void failedRefundRollsBackNewBidAndKeepsPreviousWinner() {
        Auction auction = auction();
        UUID oldId = UUID.randomUUID();
        OfflinePlayer previous = mock(OfflinePlayer.class);
        bukkit.when(() -> Bukkit.getOfflinePlayer(oldId)).thenReturn(previous);
        auction.currentTopBidder = oldId;
        auction.currentBid = 100000;
        when(economy.depositPlayer(previous, 100000)).thenReturn(failure);
        auction.doBid(bidder, 200000);
        verify(economy).depositPlayer(bidder, 200000);
        assertEquals(oldId, auction.currentTopBidder);
        assertEquals(100000, auction.currentBid, 0);
    }

    @Test
    public void rejectsExpiredNonFiniteAndUnderIncrementBids() {
        Auction auction = auction();
        auction.doBid(bidder, Double.NaN);
        auction.doBid(bidder, Double.POSITIVE_INFINITY);
        auction.doBid(bidder, 99999);
        auction.endsAt = System.currentTimeMillis() - 1;
        auction.doBid(bidder, 100000);
        verify(economy, never()).withdrawPlayer(any(OfflinePlayer.class), anyDouble());
    }

    @Test
    public void successfulLateBidExtendsAuction() {
        Auction auction = auction();
        auction.endsAt = System.currentTimeMillis() + 5000;
        long end = auction.endsAt;
        auction.doBid(bidder, 100000);
        assertEquals(end + 10000, auction.endsAt);
    }

    @Test
    public void completedAuctionDeliversRewardOnceAndPersistsHistory() {
        Auction auction = auction();
        auction.currentTopBidder = bidder.getUniqueId();
        auction.topBidderName = "NewBidder";
        auction.currentBid = 100000;
        try (MockedStatic<DataHolder> data = mockStatic(DataHolder.class);
             MockedStatic<me.recolance.auction.data.DataHandler> persistence =
                     mockStatic(me.recolance.auction.data.DataHandler.class);
             MockedStatic<Utils> utils = mockStatic(Utils.class)) {
            utils.when(() -> Utils.toBase64(any(ItemStack[].class), eq(9))).thenReturn("reward");
            auction.onEnd();
            auction.onEnd();
            data.verify(() -> DataHolder.addUnclaimedListing(eq(bidder.getUniqueId()), any()), times(1));
            persistence.verify(() -> me.recolance.auction.data.DataHandler.saveUnclaimedListing(any(), eq(false)), times(1));
            assertNull(manager.getCurrentAuction());
            assertEquals(1, manager.getPastAuctions().size());
            assertEquals(1, history.getConfig().getStringList("history").size());
            net.astropvp.auction.PastAuction saved = net.astropvp.auction.PastAuction.fromString(
                    history.getConfig().getStringList("history").get(0));
            assertEquals("NewBidder", saved.getWinningBidder());
            assertEquals(100000, saved.getWinningBid(), 0);
            verify(history).save();
        }
    }

    @Test
    public void shutdownRefundsOfflineWinnerAndClearsSingletons() {
        Auction auction = auction();
        OfflinePlayer previous = mock(OfflinePlayer.class);
        auction.currentTopBidder = UUID.randomUUID();
        auction.currentBid = 100000;
        bukkit.when(() -> Bukkit.getOfflinePlayer(auction.currentTopBidder)).thenReturn(previous);
        Field field;
        try {
            field = BlackMarketAuction.class.getDeclaredField("auctionManager");
            field.setAccessible(true);
            field.set(plugin, manager);
        } catch (ReflectiveOperationException e) { throw new AssertionError(e); }
        doCallRealMethod().when(plugin).onDisable();
        plugin.onDisable();
        verify(economy).depositPlayer(previous, 100000);
        verify(scheduler).cancelTasks(plugin);
        assertNull(manager.getCurrentAuction());
        assertNull(AuctionManager.get());
        assertNull(BlackMarketAuction.get());
    }

    @Test
    public void emptyLootDoesNotStartAuction() {
        assertFalse(manager.hasAuctionItem());
        try {
            manager.startRandomAuction();
            fail("Expected actionable missing loot error");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("No auction loot"));
        }
        assertNull(manager.getCurrentAuction());
    }

    @Test
    public void configuredIncrementAndExpiryTaskDriveRecoveredAuction() {
        config.set("minimum-bid-increment", 250000);
        when(plugin.getNextAuctionItem()).thenReturn(item);
        BukkitTask task = mock(BukkitTask.class);
        when(task.getTaskId()).thenReturn(123);
        when(scheduler.runTaskTimer(eq(plugin), any(Runnable.class), eq(0L), eq(20L))).thenReturn(task);
        try (MockedStatic<Utils> utils = mockStatic(Utils.class)) {
            utils.when(() -> Utils.toBase64(any(ItemStack[].class), eq(9))).thenReturn("reward");
            manager.startRandomAuction();
            Auction active = manager.getCurrentAuction();
            assertEquals(250000, active.bidIncrement, 0);
            assertTrue(manager.getLastAuction() > 0);
            verify(plugin).setNextAuctionItem(null);
            org.mockito.ArgumentCaptor<Runnable> timer = org.mockito.ArgumentCaptor.forClass(Runnable.class);
            verify(scheduler).runTaskTimer(eq(plugin), timer.capture(), eq(0L), eq(20L));
            active.endsAt = System.currentTimeMillis() - 1;
            timer.getValue().run();
            assertNull(manager.getCurrentAuction());
            assertEquals(1, manager.getPastAuctions().size());
            verify(scheduler).cancelTask(123);
        }
    }

    @Test
    public void legacyLootLoadsAndEditsPersistBothConfigKeys() {
        config.set("possible-items", java.util.Collections.singletonList("legacy-item"));
        try (MockedStatic<net.astropvp.util.ConfigUtils> encoding =
                     mockStatic(net.astropvp.util.ConfigUtils.class)) {
            encoding.when(() -> net.astropvp.util.ConfigUtils.getItem("legacy-item")).thenReturn(item);
            encoding.when(() -> net.astropvp.util.ConfigUtils.toBase64(any(ItemStack[].class), eq(9)))
                    .thenReturn("serialized-item");
            AuctionManager loaded = new AuctionManager();
            assertEquals(1, loaded.getPossibleItems().size());
            assertTrue(loaded.addPossibleItem(item));
            assertEquals(2, config.getStringList("items").size());
            assertEquals(config.getStringList("items"), config.getStringList("possible-items"));
            assertTrue(loaded.removePossibleItem(item));
            assertTrue(loaded.removePossibleItem(item));
            assertTrue(config.getStringList("items").isEmpty());
            assertTrue(config.getStringList("possible-items").isEmpty());
        }
    }

    @Test
    public void unknownHistoryRecordsSurviveSavingRecoveredHistory() {
        history.getConfig().set("history", java.util.Collections.singletonList("unrecognized-record"));
        AuctionManager loaded = new AuctionManager();
        loaded.addPastAuction(new net.astropvp.auction.PastAuction("Winner", 100000, "reward", 42));
        assertEquals("unrecognized-record", history.getConfig().getStringList("history").get(0));
        assertEquals(2, history.getConfig().getStringList("history").size());
    }

    @Test
    public void missingEconomyDisablesPluginBeforeScheduling() {
        Server server = mock(Server.class);
        ServicesManager services = mock(ServicesManager.class);
        PluginManager plugins = mock(PluginManager.class);
        when(plugin.getServer()).thenReturn(server);
        when(server.getServicesManager()).thenReturn(services);
        when(server.getPluginManager()).thenReturn(plugins);
        doCallRealMethod().when(plugin).onEnable();
        plugin.onEnable();
        verify(plugins).disablePlugin(plugin);
        verifyNoInteractions(scheduler);
    }

    @Test
    public void shutdownClosesTrackedMenusAndRemovesClosedViewers() throws Exception {
        net.astropvp.listener.MenuListener listener = new net.astropvp.listener.MenuListener();
        Inventory inventory = mock(Inventory.class);
        when(inventory.getHolder()).thenReturn(mock(net.astropvp.menu.Menu.class));
        org.bukkit.event.inventory.InventoryOpenEvent open = mock(org.bukkit.event.inventory.InventoryOpenEvent.class);
        when(open.getInventory()).thenReturn(inventory);
        when(open.getPlayer()).thenReturn(bidder);
        java.lang.reflect.Method onOpen = listener.getClass().getDeclaredMethod("onOpen", org.bukkit.event.inventory.InventoryOpenEvent.class);
        onOpen.setAccessible(true);
        onOpen.invoke(listener, open);
        listener.closeOpenMenus();
        verify(bidder).closeInventory();
        onOpen.invoke(listener, open);
        org.bukkit.event.inventory.InventoryCloseEvent close = mock(org.bukkit.event.inventory.InventoryCloseEvent.class);
        when(close.getInventory()).thenReturn(inventory);
        when(close.getPlayer()).thenReturn(bidder);
        java.lang.reflect.Method onClose = listener.getClass().getDeclaredMethod("onClose", org.bukkit.event.inventory.InventoryCloseEvent.class);
        onClose.setAccessible(true);
        onClose.invoke(listener, close);
        listener.closeOpenMenus();
        verify(bidder, times(1)).closeInventory();
    }

    @Test
    public void historyPaginationShowsNewestFirstAndClampsInvalidPages() throws Exception {
        ItemFactory factory = mock(ItemFactory.class);
        ItemMeta meta = mock(ItemMeta.class);
        when(factory.getItemMeta(any(Material.class))).thenReturn(meta);
        when(factory.isApplicable(any(ItemMeta.class), any(Material.class))).thenReturn(true);
        when(factory.asMetaFor(any(ItemMeta.class), any(Material.class))).thenReturn(meta);
        bukkit.when(Bukkit::getItemFactory).thenReturn(factory);
        java.util.List<Inventory> inventories = new java.util.ArrayList<>();
        bukkit.when(() -> Bukkit.createInventory(any(), eq(54), anyString())).thenAnswer(call -> {
            Inventory inventory = mock(Inventory.class);
            inventories.add(inventory);
            return inventory;
        });
        java.util.List<net.astropvp.auction.PastAuction> past = new java.util.ArrayList<>();
        for (int i = 0; i < 46; i++) past.add(new net.astropvp.auction.PastAuction("Winner" + i, i, "invalid", i));
        Field field = AuctionManager.class.getDeclaredField("pastAuctions");
        field.setAccessible(true);
        field.set(manager, past);
        net.astropvp.menu.AuctionHistoryMenu first = new net.astropvp.menu.AuctionHistoryMenu(-1);
        assertEquals(1, first.getPage());
        assertEquals(2, first.getPages());
        verify(inventories.get(0)).setItem(eq(53), any(ItemStack.class));
        verify(inventories.get(0), never()).setItem(eq(45), any(ItemStack.class));
        net.astropvp.menu.AuctionHistoryMenu last = new net.astropvp.menu.AuctionHistoryMenu(99);
        assertEquals(2, last.getPage());
        verify(inventories.get(1)).setItem(eq(45), any(ItemStack.class));
        verify(inventories.get(1), never()).setItem(eq(53), any(ItemStack.class));
        verify(inventories.get(1)).setItem(eq(0), any(ItemStack.class));
        verify(inventories.get(1), never()).setItem(eq(1), any(ItemStack.class));
        org.mockito.ArgumentCaptor<java.util.List> lore = org.mockito.ArgumentCaptor.forClass(java.util.List.class);
        verify(meta, atLeastOnce()).setLore(lore.capture());
        assertTrue(lore.getAllValues().get(0).toString().contains("Winner45"));
    }

    @Test
    public void recoveredLootApiHonorsZeroWeightsAndSelectedEconomyStage() {
        config.set("loot-source", "loottable");
        PluginManager plugins = mock(PluginManager.class);
        when(plugins.isPluginEnabled("AstroLoottables")).thenReturn(true);
        bukkit.when(Bukkit::getPluginManager).thenReturn(plugins);
        net.astropvp.loottable.LootTablePlugin tablePlugin = mock(net.astropvp.loottable.LootTablePlugin.class);
        net.astropvp.loottable.data.LootTableManager tables = mock(net.astropvp.loottable.data.LootTableManager.class);
        when(tablePlugin.getLootTableManager()).thenReturn(tables);
        net.astropvp.loottable.data.LootTableData data = new net.astropvp.loottable.data.LootTableData(
                net.astropvp.loottable.struct.LootTable.BAH);
        when(tables.getLootTable(net.astropvp.loottable.struct.LootTable.BAH)).thenReturn(data);
        ItemStack nextStage = mock(ItemStack.class);
        when(nextStage.getType()).thenReturn(Material.EMERALD);
        when(nextStage.clone()).thenReturn(nextStage);
        data.addItem(net.astropvp.loottable.struct.ECO_UPDATE.SOTW, 1, item);
        data.addItem(net.astropvp.loottable.struct.ECO_UPDATE.SOTW, 0, nextStage);
        data.addItem(net.astropvp.loottable.struct.ECO_UPDATE.FIRST_FRIDAY, 1, nextStage);
        try (MockedStatic<net.astropvp.loottable.LootTablePlugin> singleton =
                     mockStatic(net.astropvp.loottable.LootTablePlugin.class)) {
            singleton.when(net.astropvp.loottable.LootTablePlugin::getInstance).thenReturn(tablePlugin);
            LootTableBridge bridge = new LootTableBridge(plugin);
            assertTrue(bridge.isSelected());
            for (int i = 0; i < 20; i++) assertSame(item, bridge.randomReward());
            data.setCurrentEcoUpdateLoot(net.astropvp.loottable.struct.ECO_UPDATE.FIRST_FRIDAY);
            assertSame(nextStage, bridge.randomReward());
            data.getLootFromEcoUpdate(data.getCurrentEcoUpdateLoot()).put(nextStage, Double.NaN);
            assertFalse(bridge.hasRewards());
        }
    }

    @Test
    public void enableWiresBothCommandsMenusEventsAndScheduler() throws Exception {
        java.nio.file.Path folder = java.nio.file.Files.createTempDirectory("bah-lifecycle-");
        java.nio.file.Files.write(folder.resolve("history.yml"), "history: []\n".getBytes("UTF-8"));
        Server server = mock(Server.class);
        ServicesManager services = mock(ServicesManager.class);
        PluginManager plugins = mock(PluginManager.class);
        RegisteredServiceProvider<Economy> registration = mock(RegisteredServiceProvider.class);
        when(registration.getProvider()).thenReturn(economy);
        when(services.getRegistration(Economy.class)).thenReturn(registration);
        when(plugin.getServer()).thenReturn(server);
        when(plugin.getDataFolder()).thenReturn(folder.toFile());
        when(server.getServicesManager()).thenReturn(services);
        when(server.getPluginManager()).thenReturn(plugins);
        when(server.getScheduler()).thenReturn(scheduler);
        PluginCommand bah = mock(PluginCommand.class);
        PluginCommand loot = mock(PluginCommand.class);
        when(plugin.getCommand("bah")).thenReturn(bah);
        when(plugin.getCommand("bahloot")).thenReturn(loot);
        ItemFactory factory = mock(ItemFactory.class);
        ItemMeta meta = mock(ItemMeta.class);
        when(factory.getItemMeta(any(Material.class))).thenReturn(meta);
        when(factory.isApplicable(any(ItemMeta.class), any(Material.class))).thenReturn(true);
        when(factory.asMetaFor(any(ItemMeta.class), any(Material.class))).thenReturn(meta);
        bukkit.when(Bukkit::getItemFactory).thenReturn(factory);
        bukkit.when(() -> Bukkit.createInventory(any(), anyInt(), anyString())).thenReturn(mock(Inventory.class));
        doCallRealMethod().when(plugin).getAuctionManager();
        doCallRealMethod().when(plugin).getAuctionMenu();
        doCallRealMethod().when(plugin).getEventManager();
        doCallRealMethod().when(plugin).getHistory();
        doCallRealMethod().when(plugin).onEnable();
        plugin.onEnable();
        verify(plugins, never()).disablePlugin(plugin);
        verify(bah).setExecutor(any(net.astropvp.command.CommandBAH.class));
        verify(loot).setExecutor(any(net.astropvp.command.CommandBAHLoot.class));
        verify(plugins).registerEvents(any(net.astropvp.listener.MenuListener.class), eq(plugin));
        verify(scheduler).scheduleSyncRepeatingTask(eq(plugin), any(Runnable.class), eq(0L), eq(1L));
        verify(scheduler).runTaskTimer(eq(plugin), any(Runnable.class), eq(20L), eq(20L));
        assertNotNull(plugin.getAuctionManager());
        assertNotNull(plugin.getAuctionMenu());
        assertNotNull(plugin.getEventManager());
        java.nio.file.Files.delete(folder.resolve("history.yml"));
        java.nio.file.Files.delete(folder);
    }
}
