/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.data;

import me.recolance.auction.Auction;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;
    private static Plugin plugin;
    private static FileConfiguration config;

    static {
        plugin = Auction.plugin;
        config = plugin.getConfig();
    }

    public static void openDatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + config.getString("sql.host") + ":" + config.getString("sql.port") + "/" + config.getString("sql.database"), config.getString("sql.user"), config.getString("sql.pass"));
            DataHandler.generateTables();
        } catch (SQLException e) {
            System.out.println("ERROR CONNECTING TO DATABASE!");
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("COULD NOT CLOSE CONNECTION!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Database.openDatabaseConnection();
            }
        } catch (SQLException sQLException) {
            // empty catch block
        }
        return connection;
    }
}

