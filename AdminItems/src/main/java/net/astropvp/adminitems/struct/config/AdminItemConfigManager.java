package net.astropvp.adminitems.struct.config;

import lombok.Getter;
import net.astropvp.adminitems.AdminItemsPlugin;
import net.astropvp.adminitems.struct.AdminItem;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class AdminItemConfigManager {

    @Getter
    private final Map<AdminItem, AdminItemConfig> configs = new HashMap<>();

    public AdminItemConfigManager(AdminItemsPlugin plugin) {
        for (AdminItem adminItem : plugin.getAdminItemRegister().getAdminItems()) {
            AdminItemConfig adminItemConfig = new AdminItemConfig(plugin, "adminitems/" + adminItem.getID() + ".yml", adminItem);
            configs.put(adminItem, adminItemConfig);
        }
    }

    public void setupFiles() {
        configs.values().forEach(AdminItemConfig::setupConfigFile);
    }

    public void reloadFiles() {
        configs.values().forEach(AdminItemConfig::reloadConfig);
    }

    public AdminItemConfig getAbstractConfig(AdminItem pet) {
        return configs.get(pet);
    }

    public FileConfiguration getConfig(AdminItem pet) {
        return getAbstractConfig(pet).getConfig();
    }
}
