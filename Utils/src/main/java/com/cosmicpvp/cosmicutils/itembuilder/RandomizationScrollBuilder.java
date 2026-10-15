/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity
 *  org.arkhamnetwork.enchantments.RandomizationScroll
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.struct.ItemTier;
import org.arkhamnetwork.enchantments.RandomizationScroll;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class RandomizationScrollBuilder
        extends ItemBuilder {
    private ItemTier tier;
    private int min;
    private int max;

    public RandomizationScrollBuilder(ItemTier tier, int amount) {
        super(Material.PAPER);
        this.tier = tier;
        this.amount = amount;
    }

    public RandomizationScrollBuilder(ItemTier tier, int min, int max) {
        super(Material.PAPER);
        this.tier = tier;
        this.min = min;
        this.max = max;
    }

    @Override
    public ItemStack build() {
        ItemStack scroll = RandomizationScroll.getRandomizationScroll((ItemRarity) this.tier.toItemRarity());
        if (this.amount > 1) {
            scroll.setAmount(this.amount);
        }
        if (this.max != 0 && this.min != 0) {
            scroll.setAmount(ThreadLocalRandom.current().nextInt(this.max - this.min + 1) + this.min);
        }
        return scroll;
    }
}

