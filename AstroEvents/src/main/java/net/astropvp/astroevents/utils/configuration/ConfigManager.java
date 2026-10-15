package net.astropvp.astroevents.utils.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final Map<ConfigFile, AbstractConfig> configs = new HashMap<>();

    public ConfigManager(JavaPlugin plugin) {
        for (ConfigFile configFile : ConfigFile.values()) {
            AbstractConfig abstractConfig = new AbstractConfig(plugin, configFile.getFileName());
            configs.put(configFile, abstractConfig);
        }
    }

    public void setupFiles() {
        configs.values().forEach(AbstractConfig::setupConfigFile);
    }

    public void reloadFiles() {
        configs.values().forEach(AbstractConfig::reloadConfig);
    }

    public AbstractConfig getAbstractConfig(ConfigFile configFile) {
        return configs.get(configFile);
    }

    public FileConfiguration getConfig(ConfigFile configFile) {
        return getAbstractConfig(configFile).getConfig();
    }

    @Getter
    @RequiredArgsConstructor
    public enum ConfigFile {
        PARKOUR("events/parkour.yml"),
        SUMO("events/sumo.yml"),
        SPLEEF("events/spleef.yml"),
        LANGUAGE("language.yml")
        ;

        private final String fileName;

    }

}
