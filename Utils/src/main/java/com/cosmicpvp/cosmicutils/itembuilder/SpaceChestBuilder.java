/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.struct.ItemTier;
import com.cosmicpvp.cosmicutils.utils.CustomItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpaceChestBuilder
        extends ItemBuilder {
    private ItemTier tier;

    public SpaceChestBuilder(ItemTier tier) {
        super(Material.CHEST);
        this.tier = tier;
    }

    @Override
    public ItemStack build() {
        if (!CosmicUtils.get().isCosmicGalaxy()) {
            return null;
        }
        ItemStack retr = CustomItemUtils.getLootCrate(this.tier.toItemRarity());
        if (retr != null && this.amount > 1) {
            retr.setAmount(this.amount);
        }
        return retr;
    }
}

