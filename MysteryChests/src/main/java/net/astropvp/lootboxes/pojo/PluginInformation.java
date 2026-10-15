/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.astropvp.lootboxes.pojo;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PluginInformation {
    private final Class<? extends JavaPlugin> pluginClass;
    private final String pluginName;
    private final String pluginVersion;
    private final List<String> authors;
    private final List<String> dependencies;

    public PluginInformation(JavaPlugin plugin) {
        this.pluginClass = plugin.getClass();
        this.pluginName = plugin.getName();
        this.pluginVersion = plugin.getDescription().getVersion();
        this.authors = plugin.getDescription().getAuthors();
        this.dependencies = plugin.getDescription().getDepend();
    }

    public Class<? extends JavaPlugin> getPluginClass() {
        return this.pluginClass;
    }

    public String getPluginName() {
        return this.pluginName;
    }

    public String getPluginVersion() {
        return this.pluginVersion;
    }

    public List<String> getAuthors() {
        return this.authors;
    }

    public List<String> getDependencies() {
        return this.dependencies;
    }
}

