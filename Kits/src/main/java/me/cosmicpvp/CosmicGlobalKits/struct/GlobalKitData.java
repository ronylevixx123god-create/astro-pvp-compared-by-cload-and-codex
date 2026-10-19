/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.TimeUtils
 *  org.bukkit.entity.Player
 */
package me.cosmicpvp.CosmicGlobalKits.struct;

import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.database.SQLManager;
import org.bukkit.entity.Player;

public class GlobalKitData {
    private long lastUsed;
    private int kitTier = 0;

    public GlobalKitData(long lastUsed, int kitTier) {
        this.lastUsed = lastUsed;
        this.kitTier = kitTier;
    }





    public long getLastUsed() {
        return this.lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public int getKitTier() {
        return this.kitTier;
    }

    public void setKitTier(int kitTier) {
        this.kitTier = kitTier;
    }



}

