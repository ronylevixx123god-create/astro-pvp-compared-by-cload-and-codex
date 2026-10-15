/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.sql;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.function.Consumer;

public class Database {
    protected Connection connection;
    private String url;
    private String host;
    private String username;
    private String password;
    private String database;
    private FileConfiguration config;
    private JavaPlugin owning;

    public Database(JavaPlugin owning) {
        this.owning = owning;
        if (this.config == null) {
            File configFile = new File(owning.getDataFolder(), "sqlcredentials.yml");
            if (!configFile.exists()) {
                owning.saveResource("sqlcredentials.yml", false);
            }
            this.config = YamlConfiguration.loadConfiguration(configFile);
        }
        this.host = this.config.getString("sql.hostname");
        this.username = this.config.getString("sql.username");
        this.password = this.config.getString("sql.password");
        this.database = this.config.getString("sql.database");
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
            Bukkit.getScheduler().scheduleAsyncDelayedTask(this.owning, () -> this.executeUpdate(update, false));
            return;
        }
        try {
            PreparedStatement pst = this.getConnection().prepareStatement(update);
            try {
                pst.executeUpdate();
            } finally {
                if (Collections.singletonList(pst).get(0) != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void executeUpdate(String update, boolean async, Object... params) {
        if (async && Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(this.owning, () -> this.executeUpdate(update, false, params));
            return;
        }
        try {
            PreparedStatement pst = this.getConnection().prepareStatement(update);
            try {
                int sqlIndex = 1;
                for (int i = 0; i < params.length; ++i) {
                    Object o = params[i];
                    pst.setObject(sqlIndex++, o);
                }
                pst.executeUpdate();
            } finally {
                if (Collections.singletonList(pst).get(0) != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void executeQuery(String update, boolean async, Consumer<ResultSet> consumer) {
        if (async && Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(this.owning, () -> this.executeQuery(update, false, consumer));
            return;
        }
        try {
            PreparedStatement pst = this.getConnection().prepareStatement(update);
            try {
                ResultSet rs = pst.executeQuery();
                if (rs != null) {
                    consumer.accept(rs);
                }
                if (!pst.isClosed()) {
                    pst.close();
                }
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                return;
            } finally {
                if (Collections.singletonList(pst).get(0) != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            consumer.accept(null);
            return;
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
            if (this.connection != null) {
                if (this.connection.isValid(1)) {
                    return this.connection;
                }
                this.connection.close();
            }
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.connection;
    }
}

