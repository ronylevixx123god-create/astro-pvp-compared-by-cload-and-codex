package org.arkhamnetwork.enchantments;

import org.arkhamnetwork.enchantments.libs.OrbType;
import org.arkhamnetwork.enchantments.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentOrb {
    public static ItemStack getEnchantmentOrb(final int successRate, final int maxSlots, final OrbType type) {
        return ItemUtils.buildItem(Material.EYE_OF_ENDER, (short) 0, ChatColor.GOLD + "" + ChatColor.BOLD + type.label + " Enchantment Orb [" + ChatColor.GREEN.toString() + ChatColor.UNDERLINE + maxSlots + ChatColor.GOLD + "" + ChatColor.BOLD + "]", ChatColor.GREEN.toString() + "" + successRate + "% Success Rate", "", ChatColor.GOLD + "+1 Enchantment Slots", ChatColor.GOLD + "" + maxSlots + " Max Enchantment Slots", "", ChatColor.YELLOW + "Increases the # of enchantment", ChatColor.YELLOW + "slots on a piece of " + type.name().toLowerCase() + " by 1,", ChatColor.YELLOW + "up to a maximum of " + maxSlots + ".", ChatColor.GRAY + "Drag n' Drop onto " + type.name().toLowerCase() + " to apply.");
    }

    public static boolean isEnchantmentOrb(final ItemStack i) {
        return i != null && i.getType() == Material.EYE_OF_ENDER && i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().get(0).contains("Success Rate");
    }

    public static int getEnchantmentOrbSuccessRate(final ItemStack i) {
        if (!isEnchantmentOrb(i)) {
            return 0;
        }
        return Integer.parseInt(ChatColor.stripColor((String) i.getItemMeta().getLore().get(0)).split(" ")[0].replace("%", ""));
    }

    public static int getEnchantmentOrbMaxSlots(final ItemStack i) {
        if (!isEnchantmentOrb(i)) {
            return 0;
        }
        return Integer.parseInt(ChatColor.stripColor((String) i.getItemMeta().getLore().get(3)).split(" ")[0]);
    }

    public static OrbType getEnchantmentOrbType(final ItemStack orb) {
        try {
            final String enumId = ChatColor.stripColor(orb.getItemMeta().getDisplayName().split(" ")[0]).toUpperCase();
            return OrbType.valueOf(enumId);
        } catch (Exception err) {
            return OrbType.ARMOR;
        }
    }

    public static void applyEnchantmentOrb(final ItemStack item, final int maxSlots) {
        final List<String> lore = item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : new ArrayList<String>();
        final List<String> newLore = new ArrayList<String>();
        for (final String s : lore) {
            if (s.startsWith(ChatColor.GREEN.toString()) && s.contains("Enchantment Slots")) {
                continue;
            }
            newLore.add(s);
        }
        newLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + maxSlots + " Enchantment Slots" + ChatColor.GRAY + " (Orb [" + ChatColor.GREEN + "+" + (maxSlots - 9) + ChatColor.GRAY + "])");
        final ItemMeta im = item.getItemMeta();
        im.setLore((List) newLore);
        item.setItemMeta(im);
    }

    public static int getExtraEnchantmentSlots(final ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return 0;
        }
        for (final String s : item.getItemMeta().getLore()) {
            if (s.startsWith(ChatColor.GREEN.toString()) && s.contains("Enchantment Slots")) {
                return Integer.parseInt(ChatColor.stripColor(s.split("\\+")[1].split("]")[0]));
            }
        }
        return 0;
    }
}
