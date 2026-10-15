package net.astropvp.adminitems.struct.config;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.AccessLevel;
import lombok.Getter;

import net.astropvp.adminitems.AdminItemsPlugin;
import net.astropvp.adminitems.struct.AdminItem;
import net.astropvp.adminitems.struct.annotations.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

@Getter
public class AdminItemConfig {

    @Getter(value = AccessLevel.PRIVATE)
    private final AdminItemsPlugin plugin;
    private final String path;
    private FileConfiguration config;
    private final File configFile;
    private final AdminItem adminItem;

    public AdminItemConfig(AdminItemsPlugin plugin, String path, AdminItem adminItem) {
        this.plugin = plugin;
        this.path = path;
        this.adminItem = adminItem;
        this.configFile = new File(plugin.getDataFolder(), path);
    }

    public void setupConfigFile() {
        this.createDefaultFile();
        this.config = YamlConfiguration.loadConfiguration(configFile);

        this.loadVariables();

        boolean result = this.saveConfig();
        if (result) this.loadConfigFromDisk();

    }

    public boolean saveConfig() {
        try {
            config.save(configFile);
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config file for " + path);
            e.printStackTrace();
        }
        return false;
    }

    public void createDefaultFile() {
        if (!configFile.exists()) {
            try {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create config file for " + path);
                e.printStackTrace();
            }
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        this.loadVariables();
    }

    public void loadConfigFromDisk() {
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().severe("Could not load config for " + path);
            e.printStackTrace();
        }
    }

    public void loadVariables() {
        boolean needSave = false;
        for (Field field : this.adminItem.getClass().getDeclaredFields()) {

            try {
                field.setAccessible(true);

                if (field.isAnnotationPresent(ConfigValue.class)) {
                    ConfigValue annotationValue = field.getAnnotation(ConfigValue.class);

                    String name = annotationValue.name().equals("") ? field.getName() : annotationValue.name();

                    String path = annotationValue.path().endsWith(".") ? annotationValue.path() : annotationValue.path() + ".";

                    if (!this.config.contains(path + name)) {
                        this.config.set(path + name, field.get(this.adminItem));
                        needSave = true;
                        continue;
                    }

                    Object fieldValue = this.config.get(path + name);

                    if (fieldValue instanceof String) fieldValue = CC.translate((String) fieldValue);

                    field.set(this.adminItem, fieldValue);
                }
            } catch (IllegalAccessException | NullPointerException | IllegalArgumentException e) {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[CrankedAdminItems] - Error invalid config value for adminItem " + adminItem.getName()));
            }
        }
        if (needSave) {
            saveConfig();
            reloadConfig();
        }
    }
}
