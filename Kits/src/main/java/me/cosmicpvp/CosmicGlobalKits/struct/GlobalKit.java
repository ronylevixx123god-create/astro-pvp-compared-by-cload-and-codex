/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package me.cosmicpvp.CosmicGlobalKits.struct;

import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import java.util.List;
import java.util.Random;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GlobalKit {
    protected static Random random = new Random();

    public abstract List<ItemStack> getKitItems(Player var1, GlobalKits var2, int var3);

    public ItemStack setItemNameAndLore(ItemStack item, String name, List<String> lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    public ItemStack setItemNameAndLore(ItemStack item, String name, String ... lore) {
        return this.setItemNameAndLore(item, name, Lists.newArrayList(lore));
    }

    public static ItemStack addRandomEnchantLevel(ItemStack item, String enchant) {
        CustomEnchantment enchantment = EnchantmentAPI.getEnchantment((String)enchant);
        if (enchantment == null) {
            Bukkit.getLogger().info("Invalid enchantment given: " + enchant);
            return item;
        }
        return enchantment.addToItem(item, random.nextInt(enchantment.getMaxLevel()) + 1);
    }

    public static ItemStack addEnchant(ItemStack item, String enchant, int level) {
        CustomEnchantment enchantment = EnchantmentAPI.getEnchantment((String)enchant);
        if (enchantment == null) {
            Bukkit.getLogger().info("Invalid enchantment given: " + enchant);
            return item;
        }
        return enchantment.addToItem(item, level);
    }

    public static int getMaxEnchantLevel(String enchant) {
        CustomEnchantment enchantment = EnchantmentAPI.getEnchantment((String)enchant);
        if (enchantment == null) {
            Bukkit.getLogger().info("Invalid enchantment given: " + enchant);
            return 1;
        }
        return enchantment.getMaxLevel();
    }
}

