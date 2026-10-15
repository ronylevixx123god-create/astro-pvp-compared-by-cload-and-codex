/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.struct.ItemTier;
import com.cosmicpvp.cosmicutils.utils.CustomItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class MysteryEnchantBookBuilder
        extends ItemBuilder {
    private ItemTier tier;
    private int min;
    private int max;

    public MysteryEnchantBookBuilder(ItemTier tier) {
        super(Material.BOOK);
        this.tier = tier;
    }

    public MysteryEnchantBookBuilder(ItemTier tier, int min, int max) {
        super(Material.BOOK);
        this.tier = tier;
        this.min = min;
        this.max = max;
    }

    @Override
    public ItemStack build() {
        ItemStack retr = CustomItemUtils.getMysteryEnchantmentBook(this.tier.getRarityId());
        if (this.min != 0 && this.max != 0) {
            retr.setAmount(ThreadLocalRandom.current().nextInt(this.max - this.min + 1) + this.min);
        }
        return retr;
    }
}

