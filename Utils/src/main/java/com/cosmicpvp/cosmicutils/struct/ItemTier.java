/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity
 */
package com.cosmicpvp.cosmicutils.struct;

import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;

public enum ItemTier {
    UNKNOWN("Unknown", 1),
    SIMPLE("Simple", 1),
    UNIQUE("Unique", 2),
    ELITE("Elite", 3),
    ULTIMATE("Ultimate", 4),
    LEGENDARY("Legendary", 5),
    GOD("Godly", 6),
    HEROIC("Heroic", 7),
    SOUL("Soul", 7);

    public String name;
    private int rarityId;

    private ItemTier(String name, int rarityId) {
        this.name = name;
        this.rarityId = rarityId;
    }

    public ItemRarity toItemRarity() {
        if (this == HEROIC) {
            return ItemRarity.SOUL;
        }
        return ItemRarity.valueOf((String) this.name());
    }

    public String getName() {
        return this.name;
    }

    public int getRarityId() {
        return this.rarityId;
    }
}

