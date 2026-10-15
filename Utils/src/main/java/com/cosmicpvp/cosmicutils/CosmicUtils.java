package com.cosmicpvp.cosmicutils;

import com.cosmicpvp.cosmicutils.cache.CacheManager;
import com.cosmicpvp.cosmicutils.commands.CommandCosmicDebug;
import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.listeners.GUIListener;
import com.cosmicpvp.cosmicutils.gui.listeners.PlayerListener;
import com.cosmicpvp.cosmicutils.gui.listeners.TitleItemListener;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.cosmicpvp.cosmicutils.utils.UpgradeUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.cosmicpvp.cosmicutils.utils.databases.SeenDatabase;
import com.google.common.collect.MapMaker;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CosmicUtils extends JavaPlugin {
    private static CosmicUtils instance;
    private Map<String, Player> asyncPlayerMap = new MapMaker().concurrencyLevel(32).weakValues().makeMap();
    private Map<UUID, Player> asyncPlayerMapUUID = new MapMaker().concurrencyLevel(32).weakValues().makeMap();
    private Map<String, Location> playerLocations = new ConcurrentHashMap<>();
    private InventoryManager inventoryManager;
    private boolean arkkitEnabled = false;
    private boolean vanishNoPacketEnabled = false;
    private boolean evolutionKitEnabled = false;
    private boolean hcfCrateEnabled = false;
    private boolean conquestsEnabled = false;
    private boolean enchantmentsEnabled = false;
    private boolean dungeonsEnabled = false;
    private boolean masksEnabled = false;
    private boolean globalKitsEnabled;
    private boolean rankQuestsEnabled;
    private boolean outpostsEnabled = false;
    private boolean armorsetEnabled = false;
    private boolean lootboxesEnabled;
    private String serverName;
    private boolean vanillaGalaxy;
    @Getter
    private CacheManager cacheManager;
    private boolean cosmicGalaxy;
    private boolean hcfGalaxy;
    private boolean seasonalGalaxy;
    private SeenDatabase seenDatabase;

    public static CosmicUtils get() {
        return instance;
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        this.registerCommands();
        this.registerListeners();
        WorldGuardUtils.hook();
        FactionUtils.init();
        this.seenDatabase = new SeenDatabase(this);
        if (this.doesPluginExist("CosmicHCF")) {
            this.hcfGalaxy = true;
        }
        if (this.doesPluginExist("CosmicSpaceChests-Vanilla") || this.doesPluginExist("CosmicDropPackages_Vanilla")) {
            this.vanillaGalaxy = true;
        }
            this.cosmicGalaxy = true;

        if (new File("seasonal_galaxy").exists()) {
            this.seasonalGalaxy = true;
        }
        if (this.doesPluginExist("CosmicGlobalKits")) {
            this.globalKitsEnabled = true;
        }
        for (Player player : PlayerUtil.getOnlinePlayers()) {
            this.playerLocations.put(player.getName(), player.getLocation());
            this.asyncPlayerMap.put(player.getName(), player);
            this.asyncPlayerMapUUID.put(player.getUniqueId(), player);
        }
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : PlayerUtil.getOnlinePlayers()) {
                this.playerLocations.put(player.getName(), player.getLocation());
            }
        }, 0L, 40L);
        CosmicGUI.activeGUIs.entrySet();
        this.arkkitEnabled = Bukkit.getPluginManager().isPluginEnabled("Arkkit");
        UpgradeUtils.factionUpgradesEnabled = true;
        this.armorsetEnabled = true;
        this.outpostsEnabled = true;
        this.evolutionKitEnabled = Bukkit.getPluginManager().isPluginEnabled("CosmicEvolutionKits");
        this.vanishNoPacketEnabled = Bukkit.getPluginManager().isPluginEnabled("VanishNoPacket");
        this.hcfCrateEnabled = Bukkit.getPluginManager().isPluginEnabled("CosmicHcfCrate");
        this.conquestsEnabled = true;
        this.enchantmentsEnabled = true;
        this.dungeonsEnabled = true;
        this.masksEnabled = true;
        this.lootboxesEnabled = true;
        this.inventoryManager = new InventoryManager(this);
        this.cacheManager = new CacheManager();

        this.inventoryManager.init();
        this.initStaticClasses();
    }

    @Override
    public void onDisable() {
        instance = null;
        CosmicGUI.activeGUIs.values().forEach(CosmicGUI::close);
        FactionUtils.destroy();
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    private boolean doesPluginExist(String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    private void registerCommands() {
        this.getCommand("cosmicdebug").setExecutor(new CommandCosmicDebug());
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new GUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new TitleItemListener(), this);
    }

    private void initStaticClasses() {
        try {
            Class.forName("com.cosmicpvp.cosmicutils.utils.ItemSerialization");
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().info("Error attempting to load class ItemSerialization.");
            e.printStackTrace();
        }
    }

    public Map<String, Player> getAsyncPlayerMap() {
        return this.asyncPlayerMap;
    }

    public Map<UUID, Player> getAsyncPlayerMapUUID() {
        return this.asyncPlayerMapUUID;
    }

    public Map<String, Location> getPlayerLocations() {
        return this.playerLocations;
    }

    public boolean isArkkitEnabled() {
        return this.arkkitEnabled;
    }

    public boolean isVanishNoPacketEnabled() {
        return this.vanishNoPacketEnabled;
    }

    public boolean isEvolutionKitEnabled() {
        return this.evolutionKitEnabled;
    }

    public boolean isHcfCrateEnabled() {
        return this.hcfCrateEnabled;
    }

    public boolean isConquestsEnabled() {
        return this.conquestsEnabled;
    }

    public boolean isEnchantmentsEnabled() {
        return this.enchantmentsEnabled;
    }

    public boolean isDungeonsEnabled() {
        return this.dungeonsEnabled;
    }

    public boolean isMasksEnabled() {
        return this.masksEnabled;
    }

    public boolean isGlobalKitsEnabled() {
        return this.globalKitsEnabled;
    }

    public boolean isRankQuestsEnabled() {
        return this.rankQuestsEnabled;
    }

    public boolean isOutpostsEnabled() {
        return this.outpostsEnabled;
    }

    public boolean isArmorsetEnabled() {
        return this.armorsetEnabled;
    }

    public boolean isLootboxesEnabled() {
        return this.lootboxesEnabled;
    }

    public String getServerName() {
        return this.serverName;
    }

    public boolean isVanillaGalaxy() {
        return this.vanillaGalaxy;
    }

    public boolean isCosmicGalaxy() {
        return this.cosmicGalaxy;
    }

    public boolean isHcfGalaxy() {
        return this.hcfGalaxy;
    }

    public boolean isSeasonalGalaxy() {
        return this.seasonalGalaxy;
    }

    public SeenDatabase getSeenDatabase() {
        return this.seenDatabase;
    }
}

