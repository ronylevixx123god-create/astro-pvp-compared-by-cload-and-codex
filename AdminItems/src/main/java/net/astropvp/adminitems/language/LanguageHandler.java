package net.astropvp.adminitems.language;

import net.astropvp.adminitems.AdminItemsPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class LanguageHandler {

    private final File file;
    private final FileConfiguration config;
    private final String path;
    private final AdminItemsPlugin plugin;

    public LanguageHandler(AdminItemsPlugin plugin) {
        this.plugin = plugin;
        this.path = "language.yml";
        file = new File(plugin.getDataFolder(), path);
        config = new YamlConfiguration();
    }

    public void load() {
        this.saveDefaultConfig();

        this.loadConfigFromDisk();
        this.copyDefaults();
        this.save();

        this.loadConfigFromDisk();

        this.loadLangMessages();
    }

    private void loadLangMessages() {
        for (Language entry : Language.values()) {
            String path = entry.getPath();
            if (config.isList(path)) {
                entry.setValue(config.getStringList(path));
                continue;
            }
            entry.setValue(config.getString(path));
        }
    }

    private void loadConfigFromDisk() {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config file for " + file.getPath() + "!");
            e.printStackTrace();
        }
    }

    private void copyDefaults() {
        InputStream defConfig = plugin.getResource(this.path);
        if (defConfig != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfig)));
        }
        config.options().copyDefaults(true);
    }

    private void saveDefaultConfig() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(),false);
        }
    }

}