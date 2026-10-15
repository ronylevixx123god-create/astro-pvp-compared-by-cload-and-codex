/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitTask
 */
package me.recolance.auction;

import lombok.SneakyThrows;
import me.recolance.auction.command.CommandHandler;
import me.recolance.auction.controller.ConfirmMenu;
import me.recolance.auction.controller.Controller;
import me.recolance.auction.controller.Events;
import me.recolance.auction.data.DataHandler;
import me.recolance.auction.data.Database;
import me.recolance.auction.data.SafetyToggle;
import me.recolance.auction.menu.BidListener;
import me.recolance.auction.menu.BidManager;
import me.recolance.auction.menu.MenuController;
import me.recolance.auction.tasks.ItemExpireWarnTask;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;

public class Auction
        extends JavaPlugin {
    public static Auction plugin;
    public static Economy economy;
    public static boolean stoppingServer;

    static {
        economy = null;
        stoppingServer = false;
    }

    private SafetyToggle toggles;
    private BidManager bidManager;

    public static Auction getPlugin() {
        return plugin;
    }

    @SneakyThrows
    public void onEnable() {
        plugin = this;
        this.toggles = new SafetyToggle();
        stoppingServer = false;
        this.setupEconomy();
        this.registration();
        Database.openDatabaseConnection();
        DataHandler.loadListings();
        Controller.expireListings();
        Controller.expireOfflineUnclaimedListings();
        this.bidManager = new BidManager();
        try {
            this.bidManager.loadBids(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bidManager.startBidTimer();
    }

    public void onDisable() {
        stoppingServer = true;
        this.bidManager.setProccessAuctionHouse(false);
        for (Player p : Bukkit.getOnlinePlayers()) {
            String invName = p.getInventory().getTitle();
            if (!invName.equals("Cancelled / Expired Listings") && !invName.equals("Auction House") && !invName.equals("Your Current Listings"))
                continue;
            p.closeInventory();
        }
        int attempts = 0;
        while (this.getPendingAsyncTasks() > 0 && attempts++ <= 20) {
            Bukkit.getLogger().info("(Auction) There are still " + this.getPendingAsyncTasks() + " pending SQL tasks, sleeping 500ms...");
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.toggles.saveFile();
        if (this.bidManager != null) {
            try {
                this.bidManager.saveBids(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        plugin = null;
        Database.closeConnection();
    }

    private int getPendingAsyncTasks() {
        int c = 0;
        for (BukkitTask bt : Bukkit.getScheduler().getPendingTasks()) {
            if (bt.isSync() || !bt.getOwner().equals((Object) plugin)) continue;
            ++c;
        }
        return c;
    }

    public void registration() {
        this.saveDefaultConfig();
        this.updateConfig();
        this.getServer().getPluginManager().registerEvents((Listener) new BidListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new MenuController(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new Events(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new ConfirmMenu(), (Plugin) this);
        this.getCommand("ah").setExecutor((CommandExecutor) new CommandHandler());
        new ItemExpireWarnTask().runTaskTimer((Plugin) this, 4800L, 4800L);
    }

    private void updateConfig() {
        if (!this.getConfig().contains("auction.preventCreativeSale")) {
            this.getConfig().set("auction.preventCreativeSale", (Object) false);
        }
        this.saveConfig();
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = (Economy) economyProvider.getProvider();
        }
        return economy != null;
    }

    public boolean hasToggle(Player pl) {
        return this.toggles.isToggled(pl);
    }

    public SafetyToggle getToggles() {
        return this.toggles;
    }

    public BidManager getBidManager() {
        return this.bidManager;
    }

}

