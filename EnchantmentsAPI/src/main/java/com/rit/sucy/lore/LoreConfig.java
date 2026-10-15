/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.rit.sucy.lore;

import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LoreConfig {
    private final String fileName;
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public LoreConfig(JavaPlugin plugin, String type) {
        this.plugin = plugin;
        this.fileName = type + ".yml";
        this.configFile = new File(plugin.getDataFolder(), this.fileName);
        this.saveDefaultConfig();
    }

    public Hashtable<String, List<String>> getLists() {
        Hashtable<String, List<String>> table = new Hashtable<String, List<String>>();
        for (String key : this.getConfig().getKeys(false)) {
            table.put(key, this.getConfig().getStringList(key));
        }
        return table;
    }

    private void reloadConfig() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration((File)this.configFile);
        InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration((InputStream)defConfigStream);
            this.fileConfiguration.setDefaults((Configuration)defConfig);
        }
    }

    private FileConfiguration getConfig() {
        if (this.fileConfiguration == null) {
            this.reloadConfig();
        }
        return this.fileConfiguration;
    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }
    }
}

