/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.cosmicpvp.CosmicSpaceChests.generators;

import com.cosmicpvp.CosmicSpaceChests.generators.LegendaryItems;
import java.util.Random;
import org.bukkit.inventory.ItemStack;

public class LegendaryGenerator {
    public static ItemStack getRandomLegendary() {
        return LegendaryItems.values()[new Random().nextInt(LegendaryItems.values().length)].getItem();
    }

    public static ItemStack getLegendary(String sys_name) {
        return LegendaryItems.valueOf(sys_name).getItem();
    }
}

