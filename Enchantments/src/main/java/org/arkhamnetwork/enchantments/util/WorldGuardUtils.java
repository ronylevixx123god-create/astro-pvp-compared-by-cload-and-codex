package org.arkhamnetwork.enchantments.util;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.List;

public class WorldGuardUtils {
    private static WorldGuardPlugin wg;

    static {
        WorldGuardUtils.wg = null;
    }

    public static void hook() {
        WorldGuardUtils.wg = getWorldGuard();
    }

    public static String getRegionName(final Location l) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            return "";
        }
        try {
            final Class<?> bukkitUtil = WorldGuardUtils.wg.getClass().getClassLoader().loadClass("com.sk89q.worldguard.bukkit.BukkitUtil");
            final Method toVector = bukkitUtil.getMethod("toVector", Block.class);
            final Vector blockVector = (Vector) toVector.invoke(null, l.getBlock());
            final List<String> regionSet = WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
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
        } catch (Exception ex) {
            return "";
        }
    }

    public static boolean isPvPDisabled(final Location l) {
        try {
            final Class<?> bukkitUtil = WorldGuardUtils.wg.getClass().getClassLoader().loadClass("com.sk89q.worldguard.bukkit.BukkitUtil");
            final Method toVector = bukkitUtil.getMethod("toVector", Block.class);
            final Vector blockVector = (Vector) toVector.invoke(null, l.getBlock());
            final List<String> regionSet = (List<String>) WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                try {
                    return WorldGuardUtils.wg.getGlobalRegionManager().get(l.getWorld()).getRegion("__global__").getFlags().get(DefaultFlag.PVP) == StateFlag.State.DENY;
                } catch (Exception err) {
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
                    } else {
                        if (region_priority <= return_priority) {
                            continue;
                        }
                        return_flag = (pvp_flag == StateFlag.State.DENY);
                        return_priority = region_priority;
                    }
                }
            }
            return return_flag;
        } catch (Exception ex) {
            return true;
        }
    }

    private static WorldGuardPlugin getWorldGuard() {
        final Plugin plugin = Enchantments.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
}
