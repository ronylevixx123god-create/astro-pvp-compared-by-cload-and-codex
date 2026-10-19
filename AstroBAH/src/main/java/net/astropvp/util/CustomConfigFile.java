//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.IOException;
import org.bukkit.plugin.Plugin;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class CustomConfigFile
{
    private FileConfiguration config;
    private File file;
    private Plugin plugin;
    private String fileName;

    public CustomConfigFile(final Plugin plugin, final String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.create();
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void save() {
        try {
            this.config.save(this.file);
        }
        catch (IOException e) {
            throw new IllegalStateException("Unable to save " + file, e);
        }
    }

    public void create() {
        this.file = new File(this.plugin.getDataFolder(), this.getFileName());
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.plugin.saveResource(this.getFileName(), false);
        }
        this.config = (FileConfiguration)new YamlConfiguration();
        try {
            this.config.load(this.file);
        }
        catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("Unable to load " + file, e);
        }
    }
}
