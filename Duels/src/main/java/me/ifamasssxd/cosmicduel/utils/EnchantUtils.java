/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.enchantments.EnchantmentWrapper
 */
package me.ifamasssxd.cosmicduel.utils;

import java.lang.reflect.Field;
import me.ifamasssxd.cosmicduel.utils.EnchantGlow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;

public class EnchantUtils {
    static EnchantGlow customEnchant;

    public static Enchantment getCustomEnchant() {
        if (customEnchant == null) {
            EnchantUtils.registerEnchant();
        }
        return customEnchant;
    }

    private static void registerEnchant() {
        EnchantGlow glow = new EnchantGlow(120);
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            EnchantmentWrapper.registerEnchantment((Enchantment)glow);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        customEnchant = glow;
    }
}

