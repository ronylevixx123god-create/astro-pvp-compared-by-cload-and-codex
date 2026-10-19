package com.decimatemc.blackmarketauction;

import com.decimatemc.blackmarketauction.eventgame.EventManager;
import net.astropvp.auction.AuctionManager;
import net.astropvp.command.CommandBAH;
import net.astropvp.command.CommandBAHLoot;
import net.astropvp.listener.MenuListener;
import net.astropvp.menu.AuctionMenu;
import net.astropvp.util.CustomConfigFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/** Restores the auction lifecycle recovered from the former BAH module. */
public class BlackMarketAuction extends JavaPlugin {
    private static BlackMarketAuction instance;
    private AuctionManager auctionManager;
    private AuctionMenu auctionMenu;
    private EventManager eventManager;
    private CustomConfigFile history;
    private Economy economy;
    private ItemStack nextAuctionItem;
    private LootTableBridge lootTableBridge;
    private MenuListener menuListener;

    @Override
    public void onEnable() {
        instance = this;
        try {
            saveDefaultConfig();
            if (getConfig().getInt("minimum-bid-increment", 100000) <= 0) {
                throw new IllegalStateException("minimum-bid-increment must be positive");
            }
            RegisteredServiceProvider<Economy> registration =
                    getServer().getServicesManager().getRegistration(Economy.class);
            if (registration == null || registration.getProvider() == null) {
                throw new IllegalStateException("BAH requires a registered Vault economy provider");
            }
            economy = registration.getProvider();
            history = new CustomConfigFile(this, "history.yml");
            lootTableBridge = new LootTableBridge(this);
            eventManager = new EventManager();
            auctionManager = new AuctionManager();
            auctionMenu = new AuctionMenu();
            requireCommand("bah").setExecutor(new CommandBAH());
            requireCommand("bahloot").setExecutor(new CommandBAHLoot());
            menuListener = new MenuListener();
            getServer().getPluginManager().registerEvents(menuListener, this);
            // The recovered menu documents auctions every thirty minutes.
            getServer().getScheduler().runTaskTimer(this, () -> {
                if (!auctionManager.getJournal().isPending() && auctionManager.getCurrentAuction() == null && auctionManager.hasAuctionItem()
                        && System.currentTimeMillis() - auctionManager.getLastAuction() >= 1_800_000L) {
                    auctionManager.startRandomAuction();
                }
            }, 20L, 20L);
            getLogger().info("BAH auction commands, menus, loot and event scheduler initialized");
        } catch (RuntimeException exception) {
            getLogger().log(java.util.logging.Level.SEVERE, "Unable to initialize BAH", exception);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private PluginCommand requireCommand(String name) {
        PluginCommand command = getCommand(name);
        if (command == null) throw new IllegalStateException("Missing command in plugin.yml: " + name);
        return command;
    }

    @Override
    public void onDisable() {
        try {
            if (menuListener != null) menuListener.closeOpenMenus();
            if (auctionManager != null) auctionManager.shutdown();
        } finally {
            Bukkit.getScheduler().cancelTasks(this);
            nextAuctionItem = null;
            instance = null;
        }
    }

    public static BlackMarketAuction get() { return instance; }
    public AuctionManager getAuctionManager() { return auctionManager; }
    public AuctionMenu getAuctionMenu() { return auctionMenu; }
    public EventManager getEventManager() { return eventManager; }
    public CustomConfigFile getHistory() { return history; }
    public Economy getEconomy() { return economy; }
    public LootTableBridge getLootTableBridge() { return lootTableBridge; }
    public ItemStack getNextAuctionItem() { return nextAuctionItem; }
    public void setNextAuctionItem(ItemStack item) {
        nextAuctionItem = item == null ? null : item.clone();
    }
}
