// 
// Decompiled by Procyon v0.5.36
// 

package me.ifamasssxd.cosmicconquest;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import me.ifamasssxd.cosmicconquest.listeners.ConquestListener;
import me.ifamasssxd.cosmicconquest.commands.CommandConquest;
import org.bukkit.command.CommandExecutor;
import me.ifamasssxd.cosmicconquest.commands.CommandFToken;
import me.ifamasssxd.cosmicconquest.struct.ConquestManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CosmicConquests extends JavaPlugin
{
    private static CosmicConquests instance;
    private ConquestManager manager;
    
    public void onEnable() {
        (CosmicConquests.instance = this).saveDefaultConfig();
        this.getCommand("ftoken").setExecutor(new CommandFToken());
        this.getCommand("conquests").setExecutor(new CommandConquest());
        (this.manager = new ConquestManager()).loadData();
        this.getServer().getPluginManager().registerEvents(new ConquestListener(), (Plugin)this);
    }
    
    public void onDisable() {
        this.manager.cleanup(null);
        super.onDisable();
    }
    
    public static CosmicConquests get() {
        return CosmicConquests.instance;
    }
    
    public ConquestManager getManager() {
        return this.manager;
    }
}
