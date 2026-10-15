/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package com.rit.sucy.service;

import org.bukkit.Material;

public enum MaterialClass {
    WOOD(15),
    STONE(5),
    IRON(14),
    GOLD(25),
    DIAMOND(10),
    LEATHER(15),
    CHAIN(12),
    DEFAULT(10);

    private final int enchantability;

    private MaterialClass(int enchantabilty) {
        this.enchantability = enchantabilty;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public static int getEnchantabilityFor(Material material) {
        int enchantability = DEFAULT.getEnchantability();
        for (MaterialClass materialClass : MaterialClass.values()) {
            if (!material.name().contains(materialClass.name() + "_")) continue;
            enchantability = materialClass.getEnchantability();
            break;
        }
        return enchantability;
    }
}

