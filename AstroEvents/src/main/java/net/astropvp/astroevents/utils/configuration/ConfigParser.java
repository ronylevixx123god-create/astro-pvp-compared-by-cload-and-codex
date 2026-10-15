package net.astropvp.astroevents.utils.configuration;

import net.astropvp.astroevents.utils.item.ItemBuilder;
import net.astropvp.astroevents.utils.item.SkullUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ConfigParser {

    public static ItemStack parseItem(ConfigurationSection itemSection) {
        Material itemType = Material.matchMaterial(itemSection.getString("type"));
        short data = (short) itemSection.getInt("data");
        ItemBuilder itemBuilder = new ItemBuilder(itemType, itemSection.getInt("amount", 1), data);

        if (itemSection.contains("name")) {
            itemBuilder.setName(itemSection.getString("name"));
        }

        if (itemSection.contains("lore")) {
            itemBuilder.setLore(itemSection.getStringList("lore"));
        }

        itemBuilder.addUnsafeEnchantments(parseEnchants(itemSection.getStringList("enchants")));
        itemBuilder.addItemFlags(itemSection.getStringList("flags"));

        if (itemSection.getBoolean("glowing")) {
            itemBuilder.setGlowing();
        }

        if (itemSection.getBoolean("unbreakable")) {
            itemBuilder.setUnbreakable(true);
        }

        ItemStack item = itemBuilder.colorize().build();

        if (itemSection.contains("skull-texture") && itemType == Material.SKULL_ITEM && data == 3) {
            item = SkullUtils.getSkull(item, itemSection.getString("skull-texture"));
        }

        return item;
    }

    public static Map<Enchantment, Integer> parseEnchants(List<String> toParse) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (String line : toParse) {
            String[] separation = line.split(":");
            if (separation.length == 0) continue;
            Enchantment enchant = Enchantment.getByName(separation[0].toUpperCase());
            if (enchant == null) {
                Bukkit.getLogger().warning(separation[0] + " is an invalid enchantment. Here are the options: " + Arrays.stream(Enchantment.values()).map(Enchantment::getName).collect(Collectors.joining(", ")));
                continue;
            }

            int level = separation.length > 1 ? NumberUtils.toInt(separation[1], 1) : 1;
            enchantments.put(enchant, level);
        }
        return enchantments;
    }

}