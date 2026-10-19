package me.recolance.auction.controller;

import me.recolance.auction.Auction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.*;
import org.mockito.MockedStatic;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConfirmMenuEscrowTest {
    private Auction previousPlugin;
    private Economy previousEconomy;
    private Player player;
    private PlayerInventory inventory;
    private Inventory top;
    private ItemStack item;
    private Map<String, List<MetadataValue>> metadata;
    private MockedStatic<Bukkit> bukkit;
    private MockedStatic<Controller> controller;
    private ConfirmMenu listener;

    @Before public void setup() {
        previousPlugin = Auction.plugin; previousEconomy = Auction.economy;
        Auction.plugin = mock(Auction.class); Auction.economy = mock(Economy.class);
        player = mock(Player.class); inventory = mock(PlayerInventory.class);
        when(player.getInventory()).thenReturn(inventory);
        top = mock(Inventory.class); when(top.getTitle()).thenReturn("Confirm Auction");
        InventoryView view = mock(InventoryView.class);
        when(view.getTopInventory()).thenReturn(top); when(player.getOpenInventory()).thenReturn(view);
        item = mock(ItemStack.class);
        MetadataValue escrow = mock(MetadataValue.class), price = mock(MetadataValue.class);
        when(escrow.value()).thenReturn(item); when(price.asDouble()).thenReturn(100.0);
        metadata = new HashMap<>();
        metadata.put("auctionConfirm", Collections.singletonList(escrow));
        metadata.put("auctionPrice", Collections.singletonList(price));
        when(player.hasMetadata(anyString())).thenAnswer(call -> metadata.containsKey(call.getArgument(0)));
        when(player.getMetadata(anyString())).thenAnswer(call -> metadata.get(call.getArgument(0)));
        doAnswer(call -> {metadata.remove(call.getArgument(0)); return null;})
                .when(player).removeMetadata(anyString(), eq(Auction.plugin));
        bukkit = mockStatic(Bukkit.class);
        bukkit.when(Bukkit::getScheduler).thenReturn(mock(BukkitScheduler.class));
        controller = mockStatic(Controller.class);
        controller.when(() -> Controller.getSellFee(player, 100.0)).thenReturn(10.0);
        listener = new ConfirmMenu();
    }
    @After public void cleanup() {
        controller.close(); bukkit.close(); Auction.plugin = previousPlugin; Auction.economy = previousEconomy;
    }
    private void confirm() {
        InventoryClickEvent click = mock(InventoryClickEvent.class);
        when(click.getInventory()).thenReturn(top); when(click.getClickedInventory()).thenReturn(top);
        when(click.getWhoClicked()).thenReturn(player); when(click.getRawSlot()).thenReturn(0);
        listener.onInventoryClick(click);
        verify(click).setCancelled(true);
    }
    private void close() {
        InventoryCloseEvent close = mock(InventoryCloseEvent.class);
        when(close.getInventory()).thenReturn(top); when(close.getPlayer()).thenReturn(player);
        listener.onPlayerCloseInventory(close);
    }
    @Test public void unaffordableConfirmationRetainsEscrowUntilClose() {
        confirm();
        assertEquals(2, metadata.size());
        controller.verify(() -> Controller.sell(player, item, 100.0, 10.0, false), never());
        verifyNoInteractions(inventory);
        close();
        verify(inventory).addItem(item);
        assertTrue(metadata.isEmpty());
    }
    @Test public void fullInventoryReturnsItemToWorldOnlyOnce() {
        when(inventory.firstEmpty()).thenReturn(-1);
        World world = mock(World.class); when(player.getWorld()).thenReturn(world);
        Location location = new Location(world, 1, 64, 2); when(player.getLocation()).thenReturn(location);
        confirm(); close(); close();
        verify(world, times(1)).dropItem(location, item);
        verify(inventory, never()).addItem(any(ItemStack[].class));
        assertTrue(metadata.isEmpty());
    }
    @Test public void repeatedUnaffordableClicksDoNotLoseOrDuplicateEscrow() {
        confirm(); confirm(); close(); close();
        verify(inventory, times(1)).addItem(item);
        assertTrue(metadata.isEmpty());
    }
}
