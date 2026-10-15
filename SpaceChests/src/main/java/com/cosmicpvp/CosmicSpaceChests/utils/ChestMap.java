/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.cosmicpvp.CosmicSpaceChests.utils;

import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import java.util.HashMap;
import org.bukkit.Location;

public class ChestMap
extends HashMap<Location, Long> {
    HashMap<Location, ItemRarity> chest_rarity = new HashMap();

    public long getDropTime(Location l) {
        return this.containsKey((Object)l) ? (Long)this.get((Object)l) : 0L;
    }

    public ItemRarity getChestRarity(Location l) {
        return this.chest_rarity.containsKey((Object)l) ? this.chest_rarity.get((Object)l) : ItemRarity.SIMPLE;
    }

    public void putChest(Location l, ItemRarity ir) {
        this.put(l, System.currentTimeMillis());
        this.chest_rarity.put(l, ir);
    }

    public void removeChest(Location l) {
        this.remove((Object)l);
        this.chest_rarity.remove((Object)l);
    }
}

