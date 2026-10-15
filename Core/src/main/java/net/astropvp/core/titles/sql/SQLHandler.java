/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package net.astropvp.core.titles.sql;

import java.io.File;
import java.io.IOException;

import net.astropvp.core.AstroCore;
import org.bukkit.configuration.file.YamlConfiguration;

public class SQLHandler {
    public static YamlConfiguration sqlconfig;
    public static String user;
    public static String database;
    public static String password;
    public static String port;
    public static String hostname;
    public static String sqlurl;

    public static void confirmSectionExists(String section) {
        if (!sqlconfig.contains(section)) {
            sqlconfig.createSection(section);
        }
    }

    public static void initializeSQL(AstroCore plugin) {
        sqlconfig = YamlConfiguration.loadConfiguration((File)new File(plugin.getDataFolder().getPath() + "/sqlconfig.yml"));
        SQLHandler.confirmSectionExists("user");
        SQLHandler.confirmSectionExists("database");
        SQLHandler.confirmSectionExists("password");
        SQLHandler.confirmSectionExists("port");
        SQLHandler.confirmSectionExists("hostname");
        try {
            sqlconfig.save(plugin.getDataFolder().getPath() + "/sqlconfig.yml");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        user = sqlconfig.getString("user");
        database = sqlconfig.getString("database");
        password = sqlconfig.getString("password");
        port = sqlconfig.getString("port");
        hostname = sqlconfig.getString("hostname");
        sqlurl = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoConnect=true";
    }
}

