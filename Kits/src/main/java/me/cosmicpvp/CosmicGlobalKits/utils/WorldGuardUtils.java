/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.BlockVector
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldguard.bukkit.BukkitUtil
 *  com.sk89q.worldguard.bukkit.WorldGuardPlugin
 *  com.sk89q.worldguard.protection.flags.DefaultFlag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.plugin.Plugin
 */
package me.cosmicpvp.CosmicGlobalKits.utils;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.List;

public class WorldGuardUtils {
    private static WorldGuardPlugin wg = null;

    public static void hook() {
        wg = WorldGuardUtils.getWorldGuard();
    }

    public static String getRegionName(Location l) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            return "";
        }
        try {
            Class<?> bukkitUtil = wg.getClass().getClassLoader().loadClass("com.sk89q.worldguard.bukkit.BukkitUtil");
            Method toVector = bukkitUtil.getMethod("toVector", Block.class);
            Vector blockVector = (Vector)toVector.invoke(null, new Object[]{l.getBlock()});
            List<String> regionSet = wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                return "";
            }
            String return_region = "";
            int return_priority = -1;
            for (String region : regionSet) {
                int region_priority = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();
                if (return_region.equalsIgnoreCase("")) {
                    return_region = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getId();
                    return_priority = region_priority;
                }
                if (region_priority <= return_priority) continue;
                return_region = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getId();
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

    public static boolean isPvPDisabled(Location l) {
        try {
            BlockVector blockVector = BukkitUtil.toVector(l.getBlock());
            List<String> regionSet = wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                try {
                    return wg.getGlobalRegionManager().get(l.getWorld()).getRegion("__global__").getFlags().get(DefaultFlag.PVP) == StateFlag.State.DENY;
                }
                catch (Exception err) {
                    if (err instanceof NullPointerException) {
                        return false;
                    }
                    err.printStackTrace();
                    return false;
                }
            }
            boolean return_flag = false;
            int return_priority = -1;
            for (String region : regionSet) {
                if (!wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().containsKey(DefaultFlag.PVP)) continue;
                StateFlag.State pvp_flag = (StateFlag.State)wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().get(DefaultFlag.PVP);
                int region_priority = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();
                if (return_priority == -1) {
                    return_flag = pvp_flag == StateFlag.State.DENY;
                    return_priority = region_priority;
                    continue;
                }
                if (region_priority <= return_priority) continue;
                return_flag = pvp_flag == StateFlag.State.DENY;
                return_priority = region_priority;
            }
            return return_flag;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static boolean isLavaFlowDisabled(Location l) {
        try {
            BlockVector blockVector = BukkitUtil.toVector(l.getBlock());
            List<String> regionSet = wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                try {
                    return wg.getGlobalRegionManager().get(l.getWorld()).getRegion("__global__").getFlags().get(DefaultFlag.LAVA_FLOW) == StateFlag.State.DENY;
                }
                catch (Exception err) {
                    if (err instanceof NullPointerException) {
                        return false;
                    }
                    return false;
                }
            }
            boolean return_flag = false;
            int return_priority = -1;
            for (String region : regionSet) {
                if (!wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().containsKey(DefaultFlag.PVP)) continue;
                StateFlag.State pvp_flag = (StateFlag.State)wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().get(DefaultFlag.PVP);
                int region_priority = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();
                if (return_priority == -1) {
                    return_flag = pvp_flag == StateFlag.State.DENY;
                    return_priority = region_priority;
                    continue;
                }
                if (region_priority <= return_priority) continue;
                return_flag = pvp_flag == StateFlag.State.DENY;
                return_priority = region_priority;
            }
            return return_flag;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }
}

