/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sk89q.worldguard.bukkit.WorldGuardPlugin
 *  com.sk89q.worldguard.protection.flags.DefaultFlag
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.StateFlag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  com.sk89q.worldguard.protection.regions.ProtectedRegion
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicduel.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class WorldGuardUtils {
    private static WorldGuardPlugin wg = null;

    public static void hook() {
        wg = WorldGuardUtils.getWorldGuard();
    }

    public static boolean isPVPAllowed(Location l) {
        return WorldGuardUtils.isAllowed(l, DefaultFlag.PVP);
    }

    public static String getRegionName(Location l) {
        ProtectedRegion region = WorldGuardUtils.getRegion(l);
        return region == null ? "" : region.getId();
    }

    public static ProtectedRegion getRegion(Location l) {
        int return_priority = -1;
        ProtectedRegion region = null;
        for (ProtectedRegion pr : wg.getRegionManager(l.getWorld()).getApplicableRegions(l)) {
            if (pr.getPriority() <= return_priority) continue;
            return_priority = pr.getPriority();
            region = pr;
        }
        return region;
    }

    public static boolean isAllowed(Location l, StateFlag flag) {
        int return_priority = -1;
        ProtectedRegion region = null;
        for (ProtectedRegion pr : wg.getRegionManager(l.getWorld()).getApplicableRegions(l)) {
            if (pr.getPriority() <= return_priority) continue;
            return_priority = pr.getPriority();
            region = pr;
        }
        return region != null && region.getId() != null && !region.getId().contains("__global") && region.getFlag((Flag)flag) != null && region.getFlag((Flag)flag) == StateFlag.State.ALLOW;
    }

    private static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }

    public static boolean isHighEnoughVersion() {
        return WorldGuardUtils.getWorldGuard().getDescription().getVersion().contains("6.");
    }
}

