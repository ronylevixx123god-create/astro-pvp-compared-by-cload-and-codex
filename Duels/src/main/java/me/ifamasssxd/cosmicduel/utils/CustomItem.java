/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicduel.utils;

import me.ifamasssxd.cosmicduel.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class CustomItem {
    ItemStack is;

    public CustomItem(ItemStack is) {
        this.is = is;
    }

    public CustomItem(Material mat) {
        this.is = new ItemStack(mat, 1);
    }

    public CustomItem(Material mat, int amount, String name, String ... lore) {
        this.is = ItemBuilder.buildItem(mat, amount, (short)0, name, lore);
    }

    public CustomItem(Material mat, String name) {
        this.is = ItemBuilder.buildItem(mat, name, new String[0]);
    }

    public CustomItem addEnchantment(Enchantment ench, int level) {
        if (ench == null) {
            return this;
        }
        this.is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public CustomItem setDurability(short du) {
        this.is.setDurability(du);
        return this;
    }

    public ItemStack buildItem() {
        return this.is;
    }
}

