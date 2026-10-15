/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import org.bukkit.ChatColor;

class PlayerEnterParkourB
extends RegionEvent {
    public PlayerEnterParkourB(Dungeon d, String regionName) {
        super(d, regionName);
    }

    @Override
    public void run() {
        this.dungeon.broadcastMessage((Object)ChatColor.WHITE + "Reach the Teleportation Room before the ship collapses. There is something profoundly different about this Yijki, it must be contained and neutralized at all costs!");
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 480000L / (long)(this.dungeon.isHalfTime() ? 2 : 1), "Complete Cargo Bay");
    }
}

