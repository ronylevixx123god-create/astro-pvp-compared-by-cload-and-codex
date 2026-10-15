/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.ArmorSet
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class HeroicUpgradeBuilder
        extends ItemBuilder {
    private String armorSetName;
    private boolean random;
    private double percent;
    private double min;
    private double max;

    public HeroicUpgradeBuilder(String armorSetName, double min, double max) {
        super(Material.EMERALD);
        this.min = min;
        this.max = max;
        this.armorSetName = armorSetName;
    }

    public HeroicUpgradeBuilder(boolean random, double min, double max) {
        super(Material.EMERALD);
        this.min = min;
        this.max = max;
        this.random = random;
    }

    public HeroicUpgradeBuilder(String armorSetName, double percent) {
        super(Material.EMERALD);
        this.percent = percent;
        this.armorSetName = armorSetName;
    }

    @Override
    public ItemStack build() {
        ArmorSetType set =  ArmorSetType.valueOf((String) this.armorSetName);
        return AstroArmorSetsAPI.createArmorSetUpgrade(set, (double) (this.percent != 0.0 ? this.percent : ThreadLocalRandom.current().nextDouble(this.max - this.min + 1.0) + this.min));
    }
}

