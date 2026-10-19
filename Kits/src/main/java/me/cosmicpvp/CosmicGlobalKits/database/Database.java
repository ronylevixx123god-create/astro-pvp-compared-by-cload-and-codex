package me.cosmicpvp.CosmicGlobalKits.database;

import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collections;

import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import java.sql.Connection;

public class Database {
    private String url;
    private String host;
    private String username;
    private String password;
    private String database;
    private FileConfiguration config;
    private Connection connection;

    public Database(String sqlFileName) {
        File sqlFile = new File(CosmicGlobalKits.getPlugin().getDataFolder(), sqlFileName);
        if (!sqlFile.exists()) {
            CosmicGlobalKits.getPlugin().saveResource("sqlconfig.yml", true);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)sqlFile);
        SQLSettings settings = SQLSettings.read(config);
        this.host = settings.host;
        this.username = settings.user;
        this.password = settings.password;
        this.database = settings.database;
        this.url = settings.url();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void executeUpdate(String update, boolean async) {
        if (async && Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(CosmicGlobalKits.getPlugin(), () -> this.executeUpdate(update, false));
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