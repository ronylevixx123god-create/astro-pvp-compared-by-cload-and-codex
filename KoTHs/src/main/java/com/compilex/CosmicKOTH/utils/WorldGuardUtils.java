package com.compilex.CosmicKOTH.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.List;

public class WorldGuardUtils
{
    private static WorldGuardPlugin wg;

    public static void hook() {
        WorldGuardUtils.wg = getWorldGuard();
    }

    /*public static ProtectedRegion getRegionFromName(final String worldName, final String regionName) {
        if (WorldGuardUtils.wg == null) {
            hook();
        }
        World world = Bukkit.getWorld(worldName);
        RegionManager regionManager = wg.getGlobalRegionManager().get(world);
        if (regionManager != null) {
            return regionManager.getRegion(regionName);
        }
        return null;
    }*/

    public static ProtectedRegion getRegionFromName(final String worldName, final String regionName) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            hook();
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getConsoleSender().sendMessage(CC.Red + "[CosmicKOTH] ***** getRegionFromName (world) was null *****");
            return null;
        }
        RegionManager rg = WorldGuardUtils.wg.getRegionManager(world);
        if (rg != null) {
            return rg.getRegion(regionName);
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.Red + "[CosmicKOTH] ***** getRegionFromName (region manager) was null *****");
        }
        return null;
    }

    public static String getRegionName(final Location l) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            return "";
        }
        try {
            final Class<?> bukkitUtil = WorldGuardUtils.wg.getClass().getClassLoader().loadClass("com.sk89q.worldguard.bukkit.BukkitUtil");
            final Method toVector = bukkitUtil.getMethod("toVector", Block.class);
            final Vector blockVector = (Vector)toVector.invoke(null, l.getBlock());
            final List<String> regionSet = (List<String>)WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                return "";
            }
            String return_region = "";
            int return_priority = -1;
            for (final String region : regionSet) {
                final int region_priority = WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();
                if (return_region.equalsIgnoreCase("")) {
                    return_region = WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getId();
                    return_priority = region_priority;
                }
                if (region_priority > return_priority) {
                    return_region = WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getId();
                    return_priority = region_priority;
                }
            }
            if (return_region.contains("global")) {
                return_region = "";
            }
            return return_region;
        }
        catch (Exception ex) {
            return "";
        }
    }

    public static boolean isPvPDisabled(final Location l) {
        try {
            final Vector blockVector = (Vector)BukkitUtil.toVector(l.getBlock());
            final List<String> regionSet = (List<String>)WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                try {
                    return WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion("__global__").getFlags().get(DefaultFlag.PVP) == StateFlag.State.DENY;
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
            for (final String region : regionSet) {
                if (WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().containsKey(DefaultFlag.PVP)) {
                    final StateFlag.State pvp_flag = (StateFlag.State) WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().get(DefaultFlag.PVP);
                    final int region_priority = WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();
                    if (return_priority == -1) {
                        return_flag = (pvp_flag == StateFlag.State.DENY);
                        return_priority = region_priority;
                    }
                    else {
                        if (region_priority <= return_priority) {
                            continue;
                        }
                        return_flag = (pvp_flag == StateFlag.State.DENY);
                        return_priority = region_priority;
                    }
                }
            }
            return return_flag;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static boolean isLavaFlowDisabled(final Location l) {
        try {
            final Vector blockVector = (Vector)BukkitUtil.toVector(l.getBlock());
            final List<String> regionSet = (List<String>)WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                try {
                    return WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion("__global__").getFlags().get(DefaultFlag.LAVA_FLOW) == StateFlag.State.DENY;
                }
                catch (Exception err) {
                    return err instanceof NullPointerException && false;
                }
            }
            boolean return_flag = false;
            int return_priority = -1;
            for (final String region : regionSet) {
                if (WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().containsKey(DefaultFlag.PVP)) {
                    final StateFlag.State pvp_flag = (StateFlag.State) WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().get(DefaultFlag.PVP);
                    final int region_priority = WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();
                    if (return_priority == -1) {
                        return_flag = (pvp_flag == StateFlag.State.DENY);
                        return_priority = region_priority;
                    }
                    else {
                        if (region_priority <= return_priority) {
                            continue;
                        }
                        return_flag = (pvp_flag == StateFlag.State.DENY);
                        return_priority = region_priority;
                    }
                }
            }
            return return_flag;
        }
        catch (Exception ex) {
            return false;
        }
    }

    private static WorldGuardPlugin getWorldGuard() {
        final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }

    static {
        WorldGuardUtils.wg = null;
    }
}
