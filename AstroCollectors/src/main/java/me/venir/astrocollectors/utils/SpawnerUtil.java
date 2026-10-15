package me.venir.astrocollectors.utils;

import me.venir.astrocollectors.AstroCollectors;
import me.venir.astrocollectors.utils.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.EnumMap;

public class SpawnerUtil {

    private final ConfigManager configManager;
    private final EnumMap<EntityType, Integer> mobIncomeMap;
    private int tntPerHour;

    public SpawnerUtil() {
        this.configManager = AstroCollectors.INSTANCE.getConfigManager();
        this.mobIncomeMap = new EnumMap<>(EntityType.class);
        this.reloadMobIncomeMap();
        this.reloadTntIncomeValue();
    }

    public void reloadMobIncomeMap() {
        this.mobIncomeMap.clear();
        ConfigurationSection configurationSection = this.configManager.getConfig(ConfigManager.ConfigFile.SETTINGS).getConfigurationSection("spawner-income-per-minute");
        for (String key : configurationSection.getKeys(false)) {
            int value = configurationSection.getInt(key);
            EntityType entityType = EntityType.valueOf(key);
            mobIncomeMap.put(entityType, value);
        }
    }

    public void reloadTntIncomeValue() {
        this.tntPerHour = configManager.getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("tnt-per-minute");
    }

    public double getMoneyPerMinute(EntityType type) {
        try {

            return this.mobIncomeMap.get(type);
        } catch (Exception e) {
            return 0;
        }
    }

    public double getTnTPerHour() {
        try {
            return configManager.getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("tnt-per-minute");
        } catch (Exception e) {
            return 0;
        }
    }

    public double getMoneyPerSecond(EntityType type) {
        return getMoneyPerMinute(type) / 60;
    }

    public double getTntPerMinute() {
        return this.tntPerHour;
    }

    public double getTntPerSecond() {
        return getTntPerMinute() / 60;
    }
}
