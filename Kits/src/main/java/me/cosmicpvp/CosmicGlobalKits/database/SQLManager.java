/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package me.cosmicpvp.CosmicGlobalKits.database;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SQLManager {
    public static Plugin plugin;
    public static YamlConfiguration sqlconfig;
    public static String user;
    public static String database;
    public static String password;
    public static String port;
    public static String hostname;
    public static String sqlurl;
    public static volatile CopyOnWriteArrayList<String> sql_query;

    public static void confirmSectionExists(String section) {
        if (!sqlconfig.contains(section)) {
            sqlconfig.createSection(section);
            plugin.getLogger().log(Level.SEVERE, "SQLConfig.yml field " + section + " has not been set. Connection to database will not succeed. " + plugin.getName() + " shutting down...");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public static void initializeSQL(Plugin pl) {
        plugin = pl;
        sqlconfig = YamlConfiguration.loadConfiguration((File)new File(plugin.getDataFolder().getPath() + "/sqlconfig.yml"));
        SQLSettings settings = SQLSettings.read(sqlconfig);
        try {
            sqlconfig.save(plugin.getDataFolder().getPath() + "/sqlconfig.yml");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        user = settings.user;
        database = settings.database;
        password = settings.password;
        port = Integer.toString(settings.port);
        hostname = settings.host;
        sqlurl = settings.url();
    }

    static {
        sql_query = new CopyOnWriteArrayList();
    }
}

