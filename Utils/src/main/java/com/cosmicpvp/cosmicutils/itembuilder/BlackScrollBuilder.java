/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.arkhamnetwork.enchantments.EnchantmentBook
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class BlackScrollBuilder
        extends ItemBuilder {
    private boolean heroicBlack;
    private int min;
    private int max;

    public BlackScrollBuilder(boolean heroicBlackScroll, int min, int max) {
        super(Material.INK_SACK);
        this.heroicBlack = heroicBlackScroll;
        this.min = min;
        this.max = max;
    }

    @Override
    public ItemStack build() {
        if (!CosmicUtils.get().isEnchantmentsEnabled()) {
            return null;
        }
        ItemStack retr = this.heroicBlack ? EnchantmentBook.getHeroicBlackscroll((int) this.min, (int) this.max, (boolean) true) : EnchantmentBook.getBlackScroll((int) (this.min + ThreadLocalRandom.current().nextInt(this.max - this.min + 1)));
        if (this.amount > 1) {
            retr.setAmount(this.amount);
        }
        return retr;
    }
}

