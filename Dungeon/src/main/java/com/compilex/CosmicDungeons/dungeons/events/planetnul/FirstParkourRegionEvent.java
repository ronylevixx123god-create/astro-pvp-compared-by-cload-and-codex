/*
 * Decompiled with CFR 0.150.
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;

public class FirstParkourRegionEvent
extends RegionEvent {
    public FirstParkourRegionEvent(Dungeon d, String regionName) {
        super(d, regionName);
    }

    @Override
    public void run() {
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + (this.dungeon.isHalfTime() ? 570000L : 840000L), "Complete Teleportation Portal Conduit");
        this.dungeon.broadcastMessage("This area appears to be some kind of secondary security measure. There are multiple pathways leading to different portals - which is the right one?");
    }
}

