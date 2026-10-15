/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.EnchantmentStorageMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package com.rit.sucy.enchanting;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.service.MaterialClass;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class VanillaEnchantment
extends CustomEnchantment {
    Enchantment vanilla;
    Map<MaterialClass, Integer> enchantability;

    public VanillaEnchantment(Enchantment vanilla, Material[] items, String group, String[] suffixGroup, int weight, double base, double interval, int max, String name) {
        super(name, items, group, weight);
        this.base = base;
        this.interval = interval;
        this.max = max;
        this.vanilla = vanilla;
        this.weight = new HashMap();
        this.weight.put(MaterialClass.DEFAULT, weight);
        this.enchantability = new HashMap<MaterialClass, Integer>();
        for (String sGroup : suffixGroup) {
            this.suffixGroups.add(sGroup);
        }
    }

    public Enchantment getVanillaEnchant() {
        return this.vanilla;
    }

    @Override
    public String name() {
        return this.vanilla.getName();
    }

    @Override
    public ItemStack addToItem(ItemStack item, int level) {
        EnchantmentStorageMeta meta;
        if (item.getType() == Material.ENCHANTED_BOOK && (meta = (EnchantmentStorageMeta)item.getItemMeta()).hasStoredEnchant(this.vanilla)) {
            if (meta.getEnchantLevel(this.vanilla) < level) {
                meta.removeStoredEnchant(this.vanilla);
                item.setItemMeta((ItemMeta)meta);
            } else {
                return item;
            }
        }
        item.addUnsafeEnchantment(this.vanilla, level);
        return item;
    }

    @Override
    public ItemStack removeFromItem(ItemStack item) {
        EnchantmentStorageMeta meta;
        if (item.getType() == Material.ENCHANTED_BOOK && (meta = (EnchantmentStorageMeta)item.getItemMeta()).hasStoredEnchant(this.vanilla)) {
            meta.removeStoredEnchant(this.vanilla);
            item.setItemMeta((ItemMeta)meta);
            return item;
        }
        item.removeEnchantment(this.vanilla);
        return item;
    }
}

