package com.cosmicpvp.cosmicutils.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmorUtil {
    public static float getArmorDamageNullificationPercent(float armor_val) {
        if (armor_val == 0.0f) {
            return 1.0f;
        }
        return 1.0f - armor_val * 0.04f;
    }

    public static float getEnchantDamageModifier(Player pl) {
        ItemStack weapon = pl.getItemInHand();
        float damage_mod = 0.0f;
        if (weapon != null) {
            if (weapon.getType() != Material.BOW && weapon.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)) {
                damage_mod = (float) weapon.getEnchantments().get(Enchantment.DAMAGE_ALL) * 0.625f;
            }
            if (weapon.getType() == Material.BOW && weapon.getEnchantments().containsKey(Enchantment.ARROW_DAMAGE)) {
                damage_mod = 0.25f * ((float) weapon.getEnchantments().get(Enchantment.ARROW_DAMAGE) + 1.0f);
            }
        }
        return damage_mod;
    }

    public static int getArmorValue(Player pl) {
        int armor_val = 0;
        for (ItemStack is : pl.getInventory().getArmorContents()) {
            if (is == null || is.getType() == Material.AIR) continue;
            if (is.getType() == Material.LEATHER_HELMET) {
                ++armor_val;
            }
            if (is.getType() == Material.LEATHER_CHESTPLATE) {
                armor_val += 3;
            }
            if (is.getType() == Material.LEATHER_LEGGINGS) {
                armor_val += 2;
            }
            if (is.getType() == Material.LEATHER_BOOTS) {
                ++armor_val;
            }
            if (is.getType() == Material.CHAINMAIL_HELMET) {
                armor_val += 2;
            }
            if (is.getType() == Material.CHAINMAIL_CHESTPLATE) {
                armor_val += 5;
            }
            if (is.getType() == Material.CHAINMAIL_LEGGINGS) {
                armor_val += 4;
            }
            if (is.getType() == Material.CHAINMAIL_BOOTS) {
                ++armor_val;
            }
            if (is.getType() == Material.IRON_HELMET) {
                armor_val += 2;
            }
            if (is.getType() == Material.IRON_CHESTPLATE) {
                armor_val += 6;
            }
            if (is.getType() == Material.IRON_LEGGINGS) {
                armor_val += 5;
            }
            if (is.getType() == Material.IRON_BOOTS) {
                armor_val += 2;
            }
            if (is.getType() == Material.DIAMOND_HELMET) {
                armor_val += 3;
            }
            if (is.getType() == Material.DIAMOND_CHESTPLATE) {
                armor_val += 8;
            }
            if (is.getType() == Material.DIAMOND_LEGGINGS) {
                armor_val += 6;
            }
            if (is.getType() == Material.DIAMOND_BOOTS) {
                armor_val += 3;
            }
            if (is.getType() == Material.GOLD_HELMET) {
                armor_val += 2;
            }
            if (is.getType() == Material.GOLD_CHESTPLATE) {
                armor_val += 5;
            }
            if (is.getType() == Material.GOLD_LEGGINGS) {
                armor_val += 3;
            }
            if (is.getType() != Material.GOLD_BOOTS) continue;
            ++armor_val;
        }
        return armor_val;
    }
}

