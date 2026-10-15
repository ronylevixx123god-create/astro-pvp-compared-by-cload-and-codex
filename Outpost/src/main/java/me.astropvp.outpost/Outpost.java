package me.astropvp.outpost;

import lombok.Getter;
import me.astropvp.outpost.config.ConfigManager;
import me.astropvp.outpost.listener.*;
import me.astropvp.outpost.task.NoFlyTask;
import org.bukkit.plugin.java.JavaPlugin;

public class Outpost extends JavaPlugin {
    @Getter
    private static Outpost instance;
    @Getter
    private ConfigManager configManager;






    public void onEnable() {
        instance = this;
        registerTasks();
        registerListeners();

        this.configManager = new ConfigManager(this);
        this.configManager.setupFiles();

        System.out.println("[Outpost] Plugin has been enabled!");
    }


    public void onDisable() {
        System.out.println("[Outpost] Plugin has been disabled!");
        this.configManager = null;
        instance = null;
    }

    public void registerTasks() {

        new NoFlyTask().runTaskTimer(this, 20, 10);


    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new MarsListener(), this);
        getServer().getPluginManager().registerEvents(new PlutoListener(), this);
        getServer().getPluginManager().registerEvents(new CommandAliasListener(), this);
        getServer().getPluginManager().registerEvents(new BlockCommandListener(), this);
        getServer().getPluginManager().registerEvents(new WorldChange(), this);
        getServer().getPluginManager().registerEvents(new NoFlyListener(), this);


    }
}
