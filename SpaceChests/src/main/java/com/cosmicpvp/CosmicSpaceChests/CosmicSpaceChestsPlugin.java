package com.cosmicpvp.CosmicSpaceChests;

import com.cosmicpvp.CosmicSpaceChests.cmds.CommandFDP;
import com.cosmicpvp.CosmicSpaceChests.cmds.CommandFlare;
import com.cosmicpvp.CosmicSpaceChests.cmds.CommandGiveDp;
import com.cosmicpvp.CosmicSpaceChests.libs.LootGenerator;
import com.cosmicpvp.CosmicSpaceChests.libs.RankItems;
import com.cosmicpvp.CosmicSpaceChests.listeners.ChestLootSelectorListener;
import com.cosmicpvp.CosmicSpaceChests.listeners.EnvoySummonItemListener;
import com.cosmicpvp.CosmicSpaceChests.listeners.PlayerInteractChestListener;
import com.cosmicpvp.CosmicSpaceChests.listeners.SpaceFlareListener;
import com.cosmicpvp.CosmicSpaceChests.listeners.items.RankItemListener;
import com.cosmicpvp.CosmicSpaceChests.listeners.items.TitleItemListener;
import com.cosmicpvp.CosmicSpaceChests.tasks.CosmicEnvoyChestPrunerTask;
import com.cosmicpvp.CosmicSpaceChests.tasks.CosmicEnvoyPingerTask;
import com.cosmicpvp.CosmicSpaceChests.tasks.CosmicEnvoyTask;
import com.cosmicpvp.CosmicSpaceChests.tasks.HologramUpdateTask;
import com.cosmicpvp.CosmicSpaceChests.utils.ChestMap;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CosmicSpaceChestsPlugin
extends JavaPlugin {
    public static Logger log = new Logger();
    public static ChestMap chest_locations = new ChestMap();
    public static WorldGuardPlugin wg = null;
    public static String serverName = "";
    public static CosmicSpaceChestsPlugin plugin = null;
    public static boolean worldEventsEnabled = false;
    private static HologramUpdateTask hologramTask;

    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        wg = this.getWorldGuard();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            LootGenerator.generateLootTables();
            CosmicSpaceChestsPlugin.this.registerListeners();
            CosmicSpaceChestsPlugin.this.registerTasks();
            CosmicSpaceChestsPlugin.this.registerCommands();
        }, 40L);
        this.registerHolidayListeners();
        this.registerHolidayTasks();
        worldEventsEnabled = new File("plugins/CosmicWorldEvents.jar").exists();
    }

    public void onDisable() {
        this.removeAllDropPackages();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerInteractChestListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChestLootSelectorListener(), this);
        this.getServer().getPluginManager().registerEvents(new EnvoySummonItemListener(), this);
        //this.getServer().getPluginManager().registerEvents(new TitleItemListener(), this);
        this.getServer().getPluginManager().registerEvents(new RankItemListener(), this);

        this.getServer().getPluginManager().registerEvents(new SpaceFlareListener(), this);
    }

    private void registerCommands() {
        this.getCommand("fdp").setExecutor(new CommandFDP());
        this.getCommand("givedp").setExecutor(new CommandGiveDp());
        this.getCommand("flare").setExecutor(new CommandFlare());
    }

    private void registerTasks() {
        if (this.getConfig().getBoolean("cosmic_envoy.enabled")) {
            this.getServer().getScheduler().runTaskTimer(this, new CosmicEnvoyTask(this), (long)this.getConfig().getInt("cosmic_envoy.min_delay") * 1200L, (long)this.getConfig().getInt("cosmic_envoy.min_delay") * 1200L);
            if (this.getServer().getPluginManager().isPluginEnabled("Arkkit")) {
                this.getServer().getScheduler().runTaskTimer(this, new CosmicEnvoyPingerTask(this), 300L, 300L);
            } else {
                log.error("Failed to launch DropPackagePingerTask() due to no ChunkGC plugin detected.", ((Object) this).getClass());
            }
            this.getServer().getScheduler().runTaskTimerAsynchronously(this, new CosmicEnvoyChestPrunerTask(this), 300L, 300L);
            hologramTask = new HologramUpdateTask();
            hologramTask.runTaskTimer(this, 200L, 200L);
        } else {
            log.debug("Cosmic Envoys are DISABLED!", ((Object) this).getClass());
        }
    }

    public void removeAllDropPackages() {
        for (Location loc : chest_locations.keySet()) {
            if (loc.getBlock().getType() == Material.CHEST) {
                Chest c = (Chest)loc.getBlock().getState();
                c.getBlockInventory().clear();
                c.update();
                loc.getBlock().setType(Material.AIR);
                continue;
            }
            if (loc.getBlock().getType() != Material.PUMPKIN && loc.getBlock().getType() != Material.JACK_O_LANTERN) continue;
            loc.getBlock().setType(Material.AIR);
        }
        chest_locations.clear();
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }

    private void registerHolidayListeners() {
    }

    private void registerHolidayTasks() {
        if (!this.getServer().getPluginManager().isPluginEnabled("Arkkit")) {
            log.error("Failed to launch DropPackagePingerTask() due to no ChunkGC plugin detected.", ((Object) this).getClass());
        }
    }

    public static HologramUpdateTask getHologramTask() {
        return hologramTask;
    }

}

