/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits
 *  me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKits
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.utils.CustomItemUtils;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKits;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class FallenHeroBuilder
        extends ItemBuilder {
    private boolean gkit;
    private String gkitName;
    private String evolutionKitName;

    public FallenHeroBuilder(String kitName, boolean gkit) {
        super(Material.BONE);
        this.gkit = gkit;
        if (gkit) {
            this.gkitName = kitName;
        } else {
            this.evolutionKitName = kitName;
        }
    }

    @Override
    public ItemStack build() {
        ItemStack retr = null;
        if (this.gkitName == null && this.gkit || this.evolutionKitName == null && !this.gkit) {
            if (this.gkit) {
                GlobalKits globalKits = GlobalKits.values()[ThreadLocalRandom.current().nextInt(GlobalKits.values().length)];
                retr = CustomItemUtils.createGKitFallenHero(globalKits.name());
            } else {
                EvolutionKits vkit = EvolutionKits.values()[ThreadLocalRandom.current().nextInt(EvolutionKits.values().length)];
                retr = CustomItemUtils.createVKitFallenHero(vkit.name());
            }
        }
        if (this.evolutionKitName != null) {
            retr = CustomItemUtils.createVKitFallenHero(this.evolutionKitName);
        } else if (this.gkitName != null) {
            retr = CustomItemUtils.createGKitFallenHero(this.gkitName);
        }
        if (retr != null && this.amount > 1) {
            retr.setAmount(this.amount);
        }
        return retr;
    }
}

