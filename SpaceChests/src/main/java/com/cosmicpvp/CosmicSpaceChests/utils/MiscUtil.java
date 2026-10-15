/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.Vector
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 */
package com.cosmicpvp.CosmicSpaceChests.utils;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import com.sk89q.worldedit.Vector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class MiscUtil {
    public static int getHardNextInt(int range) {
        int finalInt = 0;
        for (int i = 0; i < range; ++i) {
            if (!new Random().nextBoolean()) continue;
            ++finalInt;
        }
        return finalInt;
    }

    public static String getRegionName(Location l) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            return "";
        }
        try {
            Class<?> bukkitUtil = CosmicSpaceChestsPlugin.wg.getClass().getClassLoader().loadClass("com.sk89q.worldguard.bukkit.BukkitUtil");
            Method toVector = bukkitUtil.getMethod("toVector", Block.class);
            Vector blockVector = (Vector)toVector.invoke(null, new Object[]{l.getBlock()});
            List<String> regionSet = CosmicSpaceChestsPlugin.wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                return "";
            }
            String return_region = "";
            int return_priority = -1;
            for (String region : regionSet) {
                int region_priority = CosmicSpaceChestsPlugin.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();
                if (return_region.equalsIgnoreCase("")) {
                    return_region = CosmicSpaceChestsPlugin.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getId();
                    return_priority = region_priority;
                }
                if (region_priority <= return_priority) continue;
                return_region = CosmicSpaceChestsPlugin.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getId();
                return_priority = region_priority;
            }
            if (return_region.contains("global")) {
                return_region = "";
            }
            return return_region;
        }
        catch (Exception exception) {
            return "";
        }
    }
}

