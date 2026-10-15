/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.arkhamnetwork.enchantments.EnchantmentOrb
 *  org.arkhamnetwork.enchantments.libs.OrbType
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import org.arkhamnetwork.enchantments.EnchantmentOrb;
import org.arkhamnetwork.enchantments.libs.OrbType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class EnchantmentOrbBuilder
        extends ItemBuilder {
    private int minChance;
    private int maxChance;
    private int slots;
    private OrbType orbType;

    public EnchantmentOrbBuilder(OrbType type, int slots, int minChance, int maxChance) {
        super((Material) null, 0);
        this.orbType = type;
        this.slots = slots;
        this.minChance = minChance;
        this.maxChance = maxChance;
    }

    @Override
    public ItemStack build() {
        if (!CosmicUtils.get().isEnchantmentsEnabled()) {
            return null;
        }
        int rand = this.maxChance - this.minChance;
        int chance = rand > 0 ? this.minChance + ThreadLocalRandom.current().nextInt(rand) : this.minChance;
        return EnchantmentOrb.getEnchantmentOrb((int) chance, (int) this.slots, (OrbType) this.orbType);
    }
}

