/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.utils;

import org.bukkit.*;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {
    public static String printPretty(Location location, ChatColor color, boolean bold) {
        String xyzBefore = "%sx%s %sy%s %sz%s";
        String boldText = bold ? CC.Bold : "";
        return String.format(xyzBefore, new Object[]{color, (Object) ((Object) color) + boldText + location.getBlockX(), color, (Object) ((Object) color) + boldText + location.getBlockY(), color, (Object) ((Object) color) + boldText + location.getBlockZ()});
    }

    public static Location getLocationFromString(String s, boolean loadWorld) {
        if (!s.contains(":")) {
            return null;
        }
        String[] args = s.split(":");
        if (args.length == 6) {
            String worldName = args[0];
            World world = Bukkit.getWorld(worldName);
            if (world == null && loadWorld) {
                world = Bukkit.createWorld(new WorldCreator(worldName).generateStructures(false));
            }
            return new Location(world, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
        }
        if (args.length == 4) {
            String worldName = args[0];
            World world = Bukkit.getWorld(worldName);
            if (world == null && loadWorld) {
                world = Bukkit.createWorld(new WorldCreator(worldName).generateStructures(false));
            }
            return new Location(world, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        }
        return null;
    }

    public static String getStringFromLocation(Location l) {
        return l.getWorld().getName() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ() + ":" + l.getYaw() + ":" + l.getPitch();
    }

    public static List<String> getStringsFromLocations(List<Location> locs) {
        ArrayList<String> strings = new ArrayList<String>();
        for (Location l : locs) {
            strings.add(LocationUtils.getStringFromLocation(l));
        }
        return strings;
    }

    public static List<Location> getLocationsFromStringList(List<String> strings, boolean load) {
        ArrayList<Location> locs = new ArrayList<Location>();
        for (String s : strings) {
            locs.add(LocationUtils.getLocationFromString(s, load));
        }
        return locs;
    }
}

