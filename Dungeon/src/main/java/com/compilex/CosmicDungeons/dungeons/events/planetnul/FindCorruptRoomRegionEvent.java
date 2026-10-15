/*
 * Decompiled with CFR 0.150.
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;

public class FindCorruptRoomRegionEvent
extends RegionEvent {
    public FindCorruptRoomRegionEvent(Dungeon d, String regionName) {
        super(d, regionName);
    }

    @Override
    public void run() {
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + (long)(this.dungeon.isHalfTime() ? 60000 : 120000), "Find the Corrupt Cosmonaut");
        this.dungeon.broadcastMessage("Cosmonauts - you're closing in, proceed through the dungeon to find the Corrupt Cosmonaut!");
    }
}

