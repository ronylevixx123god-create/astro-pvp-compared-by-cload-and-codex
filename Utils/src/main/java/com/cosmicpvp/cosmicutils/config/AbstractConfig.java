package com.cosmicpvp.cosmicutils.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AbstractConfig {

    private final JavaPlugin plugin;
    private final String path;
    private FileConfiguration config;
    private final File configFile;

    public AbstractConfig(JavaPlugin plugin, String path) {
        this.plugin = plugin;
        this.path = path;
        this.configFile = new File(plugin.getDataFolder(), path);
    }

    public boolean setupConfigFile() {
        boolean copyDefaults = !configFile.exists();

        this.saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(configFile);

        if (copyDefaults) this.copyDefaults();

        boolean result = this.saveConfig();
        if (result) this.loadConfigFromDisk();

        return result;
    }

    public boolean saveConfig() {
        try {
            config.save(configFile);
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config file for " + path + "!");
            e.printStackTrace();
        }
        return false;
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            InputStream defConfig = plugin.getResource(path);
            if (defConfig != null) {
                plugin.saveResource(path,false);
            } else {
                try {
                    configFile.getParentFile().mkdirs();
                    configFile.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().severe("Could not create config file for " + path + "!");
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean reloadConfig() {
        boolean copyDefaults = !configFile.exists();

        this.saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(configFile);

        if (copyDefaults) this.copyDefaults();

        this.saveConfig();
        return this.loadConfigFromDisk();
    }

    public boolean loadConfigFromDisk() {
        try {
            config.load(configFile);
            return true;
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().severe("Could not load config for " + path + "!");
            e.printStackTrace();
        }
        return false;
    }

    public void copyDefaults() {
        InputStream defConfig = plugin.getResource(path);
        if (defConfig != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfig)));
        }
        config.options().copyDefaults(true);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getPath() {
        return path;
    }

}
