/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 */
package net.astropvp.lootboxes.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class GlowUtil {
    public static ItemStack addGlow(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        return item;
    }
}

