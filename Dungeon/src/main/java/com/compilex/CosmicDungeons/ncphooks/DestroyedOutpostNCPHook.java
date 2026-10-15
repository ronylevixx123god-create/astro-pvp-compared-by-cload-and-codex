/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  fr.neatmonster.nocheatplus.checks.CheckType
 *  fr.neatmonster.nocheatplus.checks.ViolationData
 *  fr.neatmonster.nocheatplus.checks.access.IViolationInfo
 *  fr.neatmonster.nocheatplus.hooks.NCPHook
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.MetadataValue
 */
package com.compilex.CosmicDungeons.ncphooks;


import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class DestroyedOutpostNCPHook {
    public String getHookName() {
        return "dungeon-outpost-hook";
    }



    private Long getTimeSinceRecentlyDamaged(Player player) {
        List values = player.getMetadata("lastWitherDamage");
        if (values == null || values.isEmpty()) {
            return null;
        }
        return ((MetadataValue)values.get(0)).asLong();
    }
}

