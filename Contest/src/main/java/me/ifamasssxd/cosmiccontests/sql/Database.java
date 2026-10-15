/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmiccontests.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collections;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Database {
    private String url;
    private String host;
    private String username;
    private String password;
    private String database;
    private static FileConfiguration config;
    protected Connection connection;

    public Database() {
        if (config == null) {
            File configFile = new File(CosmicContests.get().getDataFolder(), "sqlcredentials.yml");
            if (!configFile.exists()) {
                CosmicContests.get().saveResource("sqlcredentials.yml", false);
            }
            config = YamlConfiguration.loadConfiguration((File)configFile);
        }
        this.host = config.getString("sql.hostname");
        this.username = config.getString("sql.username");
        this.password = config.getString("sql.password");
        this.database = config.getString("sql.database");
        this.url = "jdbc:mysql://" + this.host + ":3306/" + this.database + "?autoReconnect=true";
    }

    public Database(String host, String username, String password, String database) {
        this.username = username;
        this.password = password;
        this.database = database;
        this.url = "jdbc:mysql://" + host + ":3306/" + database + "?autoReconnect=true";
    }

    public Database(String host, String username, String password) {
        this.username = username;
        this.password = password;
        this.url = "jdbc:mysql://" + host + ":3306?autoReconnect=true";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void executeUpdate(String update, boolean async) {
        if (async && Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CosmicContests.get(), () -> this.executeUpdate(update, false));
            return;
        }
        try {
            PreparedStatement pst = this.getConnection().prepareStatement(update);
            try {
                pst.executeUpdate();
            }
            finally {
                if (Collections.singletonList(pst).get(0) != null) {
                    pst.close();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            Connection connect = this.getConnection();
            return connect != null;
        }
        catch (Exception e) {
            return false;
        }
    }

    public synchronized Connection getConnection() {
        try {
            if (this.connection != null) {
                if (this.connection.isValid(1)) {
                    return this.connection;
                }
                this.connection.close();
            }
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.connection;
    }
}

