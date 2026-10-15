package me.kingsavaqe.cagepvp;


import me.kingsavaqe.cagepvp.combattag.CombatTagManager;
import me.kingsavaqe.cagepvp.commands.WildPvPCommand;
import me.kingsavaqe.cagepvp.factions.FactionsManager;
import me.kingsavaqe.cagepvp.immune.ImmuneManager;
import me.kingsavaqe.cagepvp.playerdata.PlayerDataManager;
import me.kingsavaqe.cagepvp.tasks.MatchesTask;
import me.kingsavaqe.cagepvp.wildpvp.WildPvPManager;
import me.kingsavaqe.cagepvp.worldguard.WorldGuardManager;
import me.kingsavaqe.cagepvp.listeners.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class AstroCagePvP extends JavaPlugin {
    private static AstroCagePvP instance;

    private FileConfiguration configLoad;

    private WildPvPManager wildPvPManager;

    private PlayerDataManager playerDataManager;

    private WorldGuardManager worldGuardManager;

    private FactionsManager factionsManager;

    private ImmuneManager immuneManager;

    private CombatTagManager combatTagManager;


    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.configLoad = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
        this.wildPvPManager = new WildPvPManager(this);
        this.playerDataManager = new PlayerDataManager();
        this.factionsManager = new FactionsManager();
        this.immuneManager = new ImmuneManager(this);
        this.combatTagManager = new CombatTagManager();
        this.worldGuardManager = new WorldGuardManager();
        (new MatchesTask(this)).runTaskTimerAsynchronously(this, 0L, 20L);
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new Join(this), this);
        pluginManager.registerEvents(new Quit(this), this);
        pluginManager.registerEvents(new Move(this), this);
        pluginManager.registerEvents(new Item(this), this);
        pluginManager.registerEvents(new Death(this), this);
        pluginManager.registerEvents(new Block(this), this);
        pluginManager.registerEvents(new Potion(this), this);
        pluginManager.registerEvents(new Entity(this), this);
        pluginManager.registerEvents(new Vehicle(this), this);
        pluginManager.registerEvents(new Faction(this), this);
        pluginManager.registerEvents(new Command(this), this);
        pluginManager.registerEvents(new Interact(this), this);
        pluginManager.registerEvents(new Teleport(this), this);
        getCommand("CageFight").setExecutor(new WildPvPCommand(this));
    }

    public void onDisable() {
        if (this.wildPvPManager != null)
            this.wildPvPManager.onDisable();
    }

    public FileConfiguration getConfig() {
        return this.configLoad;
    }

    public void reloadConfig() {
        this.configLoad = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
    }

    public WildPvPManager getWildPvPManager() {
        return this.wildPvPManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }

    public WorldGuardManager getWorldGuardManager() {
        return this.worldGuardManager;
    }

    public FactionsManager getFactionsManager() {
        return this.factionsManager;
    }


    public ImmuneManager getImmuneManager() {
        return this.immuneManager;
    }

    public static AstroCagePvP getInstance() {
        return instance;
    }

    public CombatTagManager getCombatTagManager() {
        return this.combatTagManager;
    }

}
