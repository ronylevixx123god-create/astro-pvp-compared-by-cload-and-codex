package net.crankedpvp.hypeboxes.language;

import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileHandler {

    private final File languageFile;
    private final File configFile;
    private final FileConfiguration languageFileConfiguration;
    private final FileConfiguration configFileConfiguration;
    private final String languagePath;
    private final String configPath;
    private final HypeboxesPlugin plugin;

    public FileHandler(HypeboxesPlugin plugin) {
        this.plugin = plugin;
        this.languagePath = "language.yml";
        languageFile = new File(plugin.getDataFolder(), languagePath);
        languageFileConfiguration = new YamlConfiguration();

        this.configPath = "config.yml";
        configFile = new File(plugin.getDataFolder(), configPath);
        configFileConfiguration = new YamlConfiguration();
    }

    public void load() {
        this.saveDefaultLanguage();
        this.loadLanguageFromDisk();
        this.copyLanguageDefaults();
        this.saveLanguage();
        this.loadLanguageFromDisk();
        this.loadLanguageMessages();

        this.saveDefaultConfig();
        this.loadConfigFromDisk();
        this.copyConfigDefaults();
        this.saveConfig();
        this.loadConfigFromDisk();
        this.loadConfigMessages();
    }

    private void loadLanguageMessages() {
        for (Language entry : Language.values()) {
            String path = entry.getPath();
            if (languageFileConfiguration.isList(path)) {
                entry.setValue(languageFileConfiguration.getStringList(path));
                continue;
            }
            entry.setValue(languageFileConfiguration.getString(path));
        }
    }

    private void loadConfigMessages() {
        for (Config entry : Config.values()) {
            String path = entry.getPath();
            if (configFileConfiguration.isList(path)) {
                entry.setValue(configFileConfiguration.getStringList(path));
                continue;
            }
            entry.setValue(configFileConfiguration.getString(path));
        }
    }


    private void loadLanguageFromDisk() {
        try {
            languageFileConfiguration.load(languageFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadConfigFromDisk() {
        try {
            configFileConfiguration.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveLanguage() {
        try {
            languageFileConfiguration.save(languageFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config file for " + languageFile.getPath() + "!");
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            configFileConfiguration.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config file for " + configFile.getPath() + "!");
            e.printStackTrace();
        }
    }

    private void copyLanguageDefaults() {
        InputStream defConfig = plugin.getResource(this.languagePath);
        if (defConfig != null) {
            languageFileConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfig)));
        }
        languageFileConfiguration.options().copyDefaults(true);
    }

    private void copyConfigDefaults() {
        InputStream defConfig = plugin.getResource(this.configPath);
        if (defConfig != null) {
            configFileConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfig)));
        }
        configFileConfiguration.options().copyDefaults(true);
    }

    private void saveDefaultLanguage() {
        if (!languageFile.exists()) {
            languageFile.getParentFile().mkdirs();
            plugin.saveResource(languageFile.getName(),false);
        }
    }

    private void saveDefaultConfig() {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(configFile.getName(),false);
        }
    }

}