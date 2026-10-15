/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.utils.databases;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SeenDatabase {
    private static Connection connection;
    String url;
    String host;
    String username;
    String password;
    String database;

    public SeenDatabase(Plugin instance) {
        this(instance.getConfig().getString("CosmicSeen.sql.hostname"), instance.getConfig().getString("CosmicSeen.sql.username"), instance.getConfig().getString("CosmicSeen.sql.password"), instance.getConfig().getString("CosmicSeen.sql.database"));
    }

    public SeenDatabase(String host, String username, String password, String database) {
        this.username = username;
        this.password = password;
        this.database = database;
        this.url = "jdbc:mysql://" + host + ":3306/" + database;
    }

    public void executeUpdate(String update) {
        try (PreparedStatement pst = this.getConnection().prepareStatement(update);) {
            pst.execute();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            Connection connect = this.getConnection();
            return connect != null;
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized Connection getConnection() {
        try {
            if (connection != null) {
                if (connection.isValid(1)) {
                    return connection;
                }
                connection.close();
            }
            connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}

