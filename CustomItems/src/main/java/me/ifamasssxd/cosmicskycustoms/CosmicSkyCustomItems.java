package me.ifamasssxd.cosmicskycustoms;

import me.ifamasssxd.cosmicskycustoms.commands.CommandCustomItem;
import me.ifamasssxd.cosmicskycustoms.listeners.CosmicItemListener;
import me.ifamasssxd.cosmicskycustoms.managers.CustomItemManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class CosmicSkyCustomItems
extends JavaPlugin {
    private static CosmicSkyCustomItems instance;
    private CustomItemManager itemManager;

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.itemManager = new CustomItemManager(this);
        this.getServer().getPluginManager().registerEvents((Listener)new CosmicItemListener(this), (Plugin)this);
        this.getCommand("customitems").setExecutor((CommandExecutor)new CommandCustomItem());
        this.itemManager.onEnable(this);
    }

    public void log(String name) {
        this.getLogger().info(name);
    }

    public void onDisable() {
        this.itemManager.onDisable(this);
    }

    public static CosmicSkyCustomItems get() {
        return instance;
    }

    public CustomItemManager getItemManager() {
        return this.itemManager;
    }
}

