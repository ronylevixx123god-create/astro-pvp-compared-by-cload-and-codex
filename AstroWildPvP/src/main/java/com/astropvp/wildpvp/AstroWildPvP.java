package com.astropvp.wildpvp;

import fr.minuskube.inv.InventoryManager;
import com.astropvp.wildpvp.command.WildPvPCommand;
import com.astropvp.wildpvp.listener.QueueListener;
import com.astropvp.wildpvp.queue.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AstroWildPvP extends JavaPlugin {
    private static AstroWildPvP instance;
    private QueueManager queueManager;
    private InventoryManager inventoryManager;

    public void onEnable() {
        instance = this;
        this.queueManager = new QueueManager(this);
        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();
        Bukkit.getPluginManager().registerEvents(new QueueListener(this), this);
        this.getCommand("wildpvp").setExecutor(new WildPvPCommand(this));
    }



    public QueueManager getQueueManager() {
        return this.queueManager;
    }

    public static AstroWildPvP getInstance() {
        return instance;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }
}
