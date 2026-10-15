/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity
 *  org.arkhamnetwork.enchantments.EnchantmentBook
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class EnchantDustBuilder
        extends ItemBuilder {
    boolean secretDust = false;
    private ItemRarity rarity;
    private int minChance;
    private int maxChance;

    public EnchantDustBuilder(ItemRarity rarity, boolean secret, int chance, int max) {
        super((Material) null, 0);
        this.rarity = rarity;
        this.secretDust = secret;
        this.minChance = chance;
        this.maxChance = max;
    }

    @Override
    public ItemStack build() {
        ItemStack retr;
        int chance = this.minChance == this.maxChance ? 0 : this.maxChance - this.minChance;
        int newChance = chance > 0 ? ThreadLocalRandom.current().nextInt(chance) + this.minChance : this.minChance;
        ItemStack itemStack = retr = this.secretDust ? EnchantmentBook.getSecretDust((ItemRarity) this.rarity, (int) (newChance * 10)) : EnchantmentBook.getMagicDust((ItemRarity) this.rarity, (int) newChance);
        if (this.amount > 1) {
            retr.setAmount(this.amount);
        }
        return retr;
    }
}

