/*
 * Decompiled with CFR 0.150.
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;

public abstract class RegionEvent {
    protected Dungeon dungeon;
    String regionName;

    public RegionEvent(Dungeon d, String regionName) {
        this.dungeon = d;
        this.regionName = regionName;
    }

    public abstract void run();
}

