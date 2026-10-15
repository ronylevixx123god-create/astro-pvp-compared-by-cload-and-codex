/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.libs.KitItems
 *  me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI
 *  me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits
 *  me.cosmicpvp.CosmicGlobalKits.libs.MasteryKits
 *  me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKits
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.CosmicSpaceChests.libs.KitItems;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.libs.MasteryKits;
import me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKits;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class KitGemBuilder
        extends ItemBuilder {
    int min;
    int max;
    private String kitType;
    private boolean random;
    private boolean levelupVariant;
    private boolean gkit;
    private boolean mkit;

    public KitGemBuilder(String kitType, boolean gkit) {
        super(Material.EMERALD);
        this.kitType = kitType;
        this.gkit = gkit;
    }

    public KitGemBuilder(String kitType, boolean gkit, boolean mkit) {
        this(kitType, gkit);
        this.mkit = mkit;
    }

    public KitGemBuilder(boolean gkit, boolean random) {
        super(Material.EMERALD);
        this.gkit = gkit;
        this.random = random;
    }

    public KitGemBuilder(boolean gkit, boolean levelupVariant, boolean random) {
        super(Material.EMERALD);
        this.gkit = gkit;
        this.random = random;
        this.levelupVariant = levelupVariant;
    }

    public KitGemBuilder(boolean gkit, boolean mkit, boolean levelupVariant, boolean random) {
        this(gkit, levelupVariant, random);
        this.mkit = mkit;
    }

    public KitGemBuilder(String kitType, boolean gkit, int min, int max) {
        super(Material.EMERALD);
        this.kitType = kitType;
        this.gkit = gkit;
        this.min = min;
        this.max = max;
    }

    @Override
    public ItemStack build() {
        ItemStack retr = null;
        if (this.gkit) {
            try {
                GlobalKits gkit = this.random ? GlobalKits.values()[ThreadLocalRandom.current().nextInt(GlobalKits.values().length)] : GlobalKits.valueOf((String) this.kitType);
                retr = KitItems.getKitItem((GlobalKits) gkit);
            } catch (Exception gkit) {
            }
        } else if (this.mkit) {
            MasteryKits kit;
            MasteryKits masteryKits = kit = this.random ? MasteryKits.values()[ThreadLocalRandom.current().nextInt(MasteryKits.values().length)] : MasteryKits.getFromName((String) this.kitType);
            if (kit != null) {
                retr = CosmicGlobalKitsAPI.createMasteryKitItem((MasteryKits) kit, (boolean) true);
            }
        } else {
            EvolutionKits kit;
            EvolutionKits evolutionKits = kit = this.random ? EvolutionKits.values()[ThreadLocalRandom.current().nextInt(EvolutionKits.values().length)] : EvolutionKits.getFromName((String) this.kitType);
            if (kit != null) {
                ItemStack itemStack = retr = !this.levelupVariant ? kit.getClaimableItem() : kit.getLevelUpItem(true);
            }
        }
        if (retr != null && this.min != 0 && this.max != 0) {
            retr.setAmount(ThreadLocalRandom.current().nextInt(this.max - this.min + 1) + this.min);
        }
        return retr;
    }
}

