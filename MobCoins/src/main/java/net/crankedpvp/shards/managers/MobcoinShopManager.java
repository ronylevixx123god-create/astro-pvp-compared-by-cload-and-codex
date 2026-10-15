package net.crankedpvp.shards.managers;

import com.cosmicpvp.cosmicutils.serializer.item.ItemStackSerializer;
import lombok.Getter;
import lombok.Setter;
import net.crankedpvp.shards.MobcoinPlugin;
import net.crankedpvp.shards.utils.ConfigManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MobcoinShopManager {
    private Map<ItemStack, Integer> shopItems;
    public MobcoinShopManager(MobcoinPlugin plugin) {
        this.shopItems = new HashMap<>();
        load(plugin);

    }
    public int getPurchaseAbleItemsAmount(long balance) {
        int amount = 0;
        for (Integer price : shopItems.values()) {
            if (balance >= price) {
                amount++;
            }
        }
        return amount;
    }
    public void reload(MobcoinPlugin plugin) {
        this.shopItems.clear();
        MobcoinPlugin.getInstance().getConfigManager().getAbstractConfig(ConfigManager.ConfigFile.SHOP).reloadConfig();
        load(plugin);
    }
    public void load(MobcoinPlugin plugin) {
        ConfigManager configmanager = plugin.getConfigManager();
        FileConfiguration config = configmanager.getConfig(ConfigManager.ConfigFile.SHOP);
        ConfigurationSection configurationSection = config.getConfigurationSection("shop.items");
        for (String key : configurationSection.getKeys(false)) {
            int price = configurationSection.getInt(key);
            ItemStack item = ItemStackSerializer.deserialize(key);
            this.shopItems.put(item, price);
        }
    }
    public void save(MobcoinPlugin plugin) {
        ConfigManager configmanager = plugin.getConfigManager();
        FileConfiguration config = configmanager.getConfig(ConfigManager.ConfigFile.SHOP);
        ConfigurationSection configurationSection = config.getConfigurationSection("shop.items");
        for (Map.Entry<ItemStack, Integer> entry : this.shopItems.entrySet()) {
            int price = entry.getValue();
            ItemStack item = entry.getKey();
            configurationSection.set(ItemStackSerializer.serialize(item), price);
        }

        MobcoinPlugin.getInstance().getConfigManager().getAbstractConfig(ConfigManager.ConfigFile.SHOP).saveConfig();
    }

    public void deleteItemStack(ItemStack item) {
        ConfigManager configmanager = MobcoinPlugin.getInstance().getConfigManager();
        FileConfiguration config = configmanager.getConfig(ConfigManager.ConfigFile.SHOP);
        ConfigurationSection configurationSection = config.getConfigurationSection("shop.items");
        configurationSection.set(ItemStackSerializer.serialize(item), null);
        MobcoinPlugin.getInstance().getConfigManager().getAbstractConfig(ConfigManager.ConfigFile.SHOP).saveConfig();
    }

    public void addItemStack(ItemStack item, int price) {
        ConfigManager configmanager = MobcoinPlugin.getInstance().getConfigManager();
        FileConfiguration config = configmanager.getConfig(ConfigManager.ConfigFile.SHOP);
        ConfigurationSection configurationSection = config.getConfigurationSection("shop.items");
        configurationSection.set(ItemStackSerializer.serialize(item), price);
        MobcoinPlugin.getInstance().getConfigManager().getAbstractConfig(ConfigManager.ConfigFile.SHOP).saveConfig();
    }


}
