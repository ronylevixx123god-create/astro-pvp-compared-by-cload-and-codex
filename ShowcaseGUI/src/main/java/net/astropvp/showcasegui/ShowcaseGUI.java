package net.astropvp.showcasegui;

import java.sql.Connection;
import java.util.Iterator;

import net.astropvp.showcasegui.commands.CommandEditShowcase;
import net.astropvp.showcasegui.commands.CommandMergeShowcase;
import net.astropvp.showcasegui.commands.CommandSaveShowcase;
import net.astropvp.showcasegui.commands.CommandShowcase;
import net.astropvp.showcasegui.custominventory.CustomInventory;
import net.astropvp.showcasegui.custominventory.CustomInventoryListener;
import net.astropvp.showcasegui.listeners.PlayerListener;
import net.astropvp.showcasegui.sql.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ShowcaseGUI extends JavaPlugin {
    private static ShowcaseGUI instance;
    private static Database database;

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        if (!this.loadDatabase()) {
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            this.registerEvents();
            this.registerCommands();
            Iterator var1 = Bukkit.getOnlinePlayers().iterator();

            while(var1.hasNext()) {
                Player p = (Player)var1.next();
                ShowcaseAPI.loadShowcase(p.getUniqueId(), p.getName(), false);
            }

        }
    }

    public boolean loadDatabase() {
        database = new Database();
        if (database.isConnected()) {
            database.executeUpdate("CREATE TABLE IF NOT EXISTS showcases(uuid varchar(64) PRIMARY KEY, showcase longtext, size int);");
            database.executeUpdate("CREATE TABLE IF NOT EXISTS showcases(uuid varchar(64) PRIMARY KEY, showcase longtext, size int);");
            System.out.println("Connected to the database successfully!");
            return true;
        } else {
            System.out.println("Unable to connect to the database!");
            return false;
        }
    }

    public static Connection getConnection() {
        return database.getConnection();
    }

    public void onDisable() {
        CustomInventory.closeInventories();
        ShowcaseAPI.saveAllShowcases();
    }

    private void registerCommands() {
        this.getCommand("mergeshowcase").setExecutor(new CommandMergeShowcase());
        this.getCommand("saveshowcase").setExecutor(new CommandSaveShowcase());
        this.getCommand("showcase").setExecutor(new CommandShowcase());
        this.getCommand("editshowcase").setExecutor(new CommandEditShowcase());
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new CustomInventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static ShowcaseGUI get() {
        return instance;
    }
}
