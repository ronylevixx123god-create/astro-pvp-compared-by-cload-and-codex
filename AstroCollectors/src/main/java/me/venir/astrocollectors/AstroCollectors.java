package me.venir.astrocollectors;

import lombok.Getter;
import me.astropvp.astrospawners.AstroSpawners;
import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import me.venir.astrocollectors.commands.CollectorGiveCommand;
import me.venir.astrocollectors.commands.ReloadCommand;
import me.venir.astrocollectors.commands.RemoveCollectorCommand;
import me.venir.astrocollectors.listeners.collectorlisteners.BlockBreakListener;
import me.venir.astrocollectors.listeners.collectorlisteners.BlockPlaceListener;
import me.venir.astrocollectors.listeners.collectorlisteners.PlayerInteractListener;
import me.venir.astrocollectors.listeners.collectorlisteners.StackedMobDeathListener;
import me.venir.astrocollectors.listeners.spawnerlisteners.SpawnerBlockBreakListener;
import me.venir.astrocollectors.listeners.spawnerlisteners.SpawnerBlockPlaceListener;
import me.venir.astrocollectors.listeners.spawnerlisteners.SpawnerExplodeListener;
import me.venir.astrocollectors.tasks.CollectorWorthUpdateTask;
import me.venir.astrocollectors.utils.SpawnerUtil;
import me.venir.astrocollectors.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class AstroCollectors extends JavaPlugin {

    @Getter
    private ConfigManager configManager;
    @Getter
    private SpawnerUtil spawnerUtil;
    private final File collectorsFile = new File(this.getDataFolder(), "collectors.json");
    public static AstroCollectors INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.configManager = new ConfigManager(this);
        this.configManager.setupFiles();
        this.spawnerUtil = new SpawnerUtil();
        CollectorManager.DELEGATE.loadFromFile(collectorsFile);

        this.registerCommands();
        this.registerListeners();
        new CollectorWorthUpdateTask().runTaskTimer(this, 0L, 100L);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            for (Collector collector : CollectorManager.DELEGATE.collectors.values()) {
                collector.clearSpawners();
                collector.scanSpawners(collector.getLocation());
            }
            System.out.println("[Collectors] Rescanned spawners for all chunk collectors!");
        }, 140L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            CollectorManager.DELEGATE.saveToFile(collectorsFile);
            System.out.println("[Collectors] Saved collectors!");

        },60L, 60*20);


    }

    @Override
    public void onDisable() {
        CollectorManager.DELEGATE.saveToFile(collectorsFile);
    }



    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockPlaceListener(), this);
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new PlayerInteractListener(this), this);
        //pm.registerEvents(new SpawnerBlockPlaceListener(), this);
        //pm.registerEvents(new SpawnerBlockBreakListener(), this);
        //pm.registerEvents(new SpawnerExplodeListener(), this);
        pm.registerEvents(new StackedMobDeathListener(this), this);
    }

    private void registerCommands() {
        getCommand("c").setExecutor(new CollectorGiveCommand());
        getCommand("removecollector").setExecutor(new RemoveCollectorCommand());
        getCommand("creload").setExecutor(new ReloadCommand());
    }
}
