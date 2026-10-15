package org.arkhamnetwork.enchantments.util;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class EnchantmentDescriptions {
    private static HashMap<String, String> descriptions;

    static {
        EnchantmentDescriptions.descriptions = null;
    }

    private static void loadConfig() {
        EnchantmentDescriptions.descriptions = new HashMap<String, String>();
        for (final String s : Enchantments.getInstance().getConfig().getStringList("descriptions")) {
            if (s != null && s.contains("=")) {
                final String key = s.split("=", 2)[0].toLowerCase();
                final String value = s.split("=", 2)[1];
                EnchantmentDescriptions.descriptions.put(key, value);
                Bukkit.getLogger().info("Loading " + key + ", description: " + value);
            }
        }
    }

    public static void reload() {
        descriptions = null;
    }

    public static String getDescription(final CustomEnchantment ce) {
        if (EnchantmentDescriptions.descriptions == null) {
            loadConfig();
        }
        return EnchantmentDescriptions.descriptions.containsKey(ce.name().toLowerCase()) ? EnchantmentDescriptions.descriptions.get(ce.name().toLowerCase()) : (ce.getDescription() == null ? "???" : ce.getDescription());
    }
}
