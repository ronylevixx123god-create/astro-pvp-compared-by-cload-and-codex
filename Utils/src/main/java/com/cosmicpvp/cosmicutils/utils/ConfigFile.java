/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.cosmicpvp.cosmicutils.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigFile {
    private final JavaPlugin plugin;
    private final String name;
    private final File file;
    private YamlConfiguration yamlConfig;

    public ConfigFile(String name, JavaPlugin plugin) {
        this.plugin = plugin;
        this.name = name;
        this.file = new File(plugin.getDataFolder() + File.separator + name + ".yml");
    }

    public void setup() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }
        if (!this.file.exists()) {
            try {
                this.plugin.saveResource(this.name + ".yml", false);
            } catch (Exception ignore) {
                try {
                    this.file.createNewFile();
                } catch (Exception ignored) {
                    System.out.println("Could not create file: " + this.name);
                }
            }
        }
        this.yamlConfig = YamlConfiguration.loadConfiguration(this.file);
    }

    public void reload() {
        this.yamlConfig = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.yamlConfig.save(this.file);
        } catch (Exception ignored) {
            System.out.println("Could not save the " + this.file.getName() + " file.");
        }
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public String getName() {
        return this.name;
    }

    public File getFile() {
        return this.file;
    }

    public YamlConfiguration getYamlConfig() {
        return this.yamlConfig;
    }
}

