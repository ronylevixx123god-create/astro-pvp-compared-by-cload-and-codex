package org.arkhamnetwork.Arkkit;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.arkhamnetwork.Arkkit.commands.*;
import org.arkhamnetwork.Arkkit.patches.ColoredLoreTag;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Arkkit
        extends JavaPlugin {
    public static Economy economy = null;
    private static Arkkit me = null;
    public long enable_time = 0L;
    public boolean shutting_down = false;
    @Getter
    public Arkkit instance;
    private HashMap<String, ArkPlugin> loaded_patches;
    private ConcurrentHashMap<String, Player> asyncPlayerMap;
    private ConcurrentHashMap<String, Location> playerLocations;

    public static Arkkit get() {
        return me;
    }

    public void onEnable() {
        instance = this;
        this.shutting_down = false;
        me = this;
        this.loaded_patches = new HashMap();
        this.asyncPlayerMap = new ConcurrentHashMap();
        this.playerLocations = new ConcurrentHashMap();
        if (!new File("plugins/Arkkit/config.yml").exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Copying default config.yml ...");
            this.saveDefaultConfig();
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
        }
        registerCommands();
        setupEconomy();
        WorldGuardUtils.hook();
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Loading Patches ...");
        Bukkit.getScheduler().runTaskLater(this, () -> {
            try {
                Arkkit.this.convertOldVersions();
            } catch (Exception err) {
                Bukkit.getLogger().info("Failed to execute convertOldVersions()!");
                err.printStackTrace();
            }
            Arkkit.this.registerPatches();
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "  *** Compilex Patch Complete! *** ");
            Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "     Thank you for your patience!");
            Bukkit.broadcastMessage("");
        }, 20L);
        this.getServer().getPluginManager().registerEvents(new ArkkitListener(), this);
        for (Player pl : Bukkit.getOnlinePlayers()) {
            this.asyncPlayerMap.put(pl.getName(), pl);
            this.playerLocations.put(pl.getName(), pl.getLocation());
        }
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Arkkit.this.playerLocations.put(p.getName(), p.getLocation());
            }
        }, 0L, 40L);
        this.enable_time = System.currentTimeMillis();


    }

    public void onDisable() {
        this.shutting_down = true;
        try {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "   *** A Compilex Patch is loading. Please wait... *** ");
            Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "  This process occurs when Compilex applies a dynamic update.");
            Bukkit.broadcastMessage("");
        } catch (Exception err) {
            err.printStackTrace();
        }
        this.saveConfig();
        this.unregisterPatches();
    }

    private void registerCommands() {
        Bukkit.getConsoleSender().sendMessage(CC.Gold + "[Arkkit] Beginning loading net.astropvp.armorsets.commands...");
        getCommand("arkhamreload").setExecutor(new CommandArkReload(this));
        Bukkit.getConsoleSender().sendMessage(CC.Green + "[Arkkit] ArkReload registered");
        getCommand("arkregister").setExecutor(new CommandArkRegister(this));
        Bukkit.getConsoleSender().sendMessage(CC.Green + "[Arkkit] ArkRegister registered");
        getCommand("arkunregister").setExecutor(new CommandArkUnregister(this));
        Bukkit.getConsoleSender().sendMessage(CC.Green + "[Arkkit] ArkUnregister registered");
        getCommand("arklist").setExecutor(new CommandArkList());
        Bukkit.getConsoleSender().sendMessage(CC.Green + "[Arkkit] ArkList registered");
        getCommand("arkconfig").setExecutor(new CommandArkConfig(this));
        Bukkit.getConsoleSender().sendMessage(CC.Green + "[Arkkit] ArkConfig registered");
        getCommand("arkreboot").setExecutor(new CommandArkReboot());
        Bukkit.getConsoleSender().sendMessage(CC.Green + "[Arkkit] ArkReboot registered");
        getCommand("arkdebug").setExecutor(new CommandArkDebug());
        Bukkit.getConsoleSender().sendMessage(CC.Green + "[Arkkit] ArkDebug registered");
        Bukkit.getConsoleSender().sendMessage(CC.Gold + "[Arkkit] Finished loading net.astropvp.armorsets.commands...");
        Arkkit.get().registerPatch(new ColoredLoreTag(this));

    }

    private boolean setupEconomy() {
        RegisteredServiceProvider economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = (Economy) economyProvider.getProvider();
        }
        return economy != null;
    }

    public boolean registerPatch(ArkPlugin ap) {
        ArkPlugins aps = ArkPlugins.reflectArkPlugin(ap);
        if (aps == null || this.loaded_patches.containsKey(aps.getConfigName())) {
            return false;
        }
        ap.inject();
        this.loaded_patches.put(aps.getConfigName(), ap);
        return true;
    }

    public boolean unregisterPatch(String config_path) {
        ArkPlugin ap;
        ArkPlugin arkPlugin = ap = this.loaded_patches.getOrDefault(config_path, null);
        if (ap == null) {
            return false;
        }
        ap.kill();
        this.loaded_patches.remove(config_path);
        return true;
    }

    public void registerPatches() {
        for (ArkPlugins aps : ArkPlugins.values()) {
            if (!this.getConfig().contains("patches." + aps.getConfigName())) {
                this.getConfig().set("patches." + aps.getConfigName(), false);
                continue;
            }
            if (!this.getConfig().getBoolean("patches." + aps.getConfigName())) continue;
            this.registerPatch(aps.getArkPlugin());
        }
        this.saveConfig();
    }

    public void unregisterPatches() {
        for (ArkPlugin ap : this.loaded_patches.values()) {
            ap.kill();
        }
        this.loaded_patches.clear();
    }

    public boolean isRebooting() {
        return Arkkit.get().shutting_down || CommandArkReboot.restart_task_exists;
    }

    private void convertOldVersions() {
        new File("no_clean_tile_entities").delete();
        new File("no_clean_entities").delete();
        if (new File("plugins/Essentials.jar").exists()) {
            try {
                this.getConfig().set("patches." + ArkPlugins.CLEAR_INVENTORY_CONFIRM.getConfigName(), true);
                this.registerPatch(ArkPlugins.CLEAR_INVENTORY_CONFIRM.getArkPlugin());
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        // removed astrocore file deleter and giveall patch (ASTROCORE)
        this.saveConfig();
    }

    public ConcurrentHashMap<String, Player> getAsyncPlayerMap() {
        return this.asyncPlayerMap;
    }

    public ConcurrentHashMap<String, Location> getPlayerLocations() {
        return this.playerLocations;
    }
}

