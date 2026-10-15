/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import org.bukkit.ChatColor;

class PlayerEnterParkourA
extends RegionEvent {
    public PlayerEnterParkourA(Dungeon d, String regionName) {
        super(d, regionName);
    }

    @Override
    public void run() {
        this.dungeon.broadcastMessage((Object)ChatColor.WHITE + "Do not allow Yijki to escape! Move quickly, the ship is collapsing under the strain of the battle!");
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 540000L / (long)(this.dungeon.isHalfTime() ? 2 : 1), "Complete Ventilation Shaft");
    }
}

