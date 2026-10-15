/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 */
package com.cosmicpvp.CosmicSpaceChests.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LocationUtils {
    public static String convertLocationToString(Location l) {
        DecimalFormat df = new DecimalFormat("#.####");
        return l.getWorld().getName() + "," + df.format(l.getX()) + "," + df.format(l.getY()) + "," + df.format(l.getZ()) + ":" + df.format(l.getYaw()) + "$" + df.format(l.getPitch());
    }

    public static Location convertStringToLocation(String location_string) {
        return LocationUtils.convertStringToLocation(location_string, false);
    }

    public static Location convertStringToLocation(String location_string, boolean letWorldBeNull) {
        if (location_string == null) {
            return null;
        }
        if (location_string.split(",").length == 4) {
            Location loc;
            String world_name = location_string.split(",")[0];
            double x = Double.parseDouble(location_string.split(",")[1]);
            double y = Double.parseDouble(location_string.split(",")[2]);
            double z = Double.parseDouble(location_string.split(",")[3].split(":")[0]);
            float yaw = Float.parseFloat(location_string.substring(location_string.indexOf(":") + 1, location_string.indexOf("$")));
            float pitch = Float.parseFloat(location_string.substring(location_string.indexOf("$") + 1, location_string.length()));
            if (letWorldBeNull) {
                loc = new Location(Bukkit.getWorld((String)world_name), x, y, z, yaw, pitch);
            } else if (Bukkit.getWorld((String)world_name) != null) {
                loc = new Location(Bukkit.getWorld((String)world_name), x, y, z, yaw, pitch);
            } else {
                Location l_spawn = ((World)Bukkit.getServer().getWorlds().get(0)).getSpawnLocation();
                l_spawn.setPitch(2.0f);
                l_spawn.setYaw(-179.0f);
                loc = l_spawn;
            }
            return loc;
        }
        return null;
    }

    public static String convertHomeMapToString(HashMap<String, Location> hmap) {
        String s = "";
        for (Map.Entry<String, Location> data : hmap.entrySet()) {
            s = s + data.getKey() + "@" + LocationUtils.convertLocationToString(data.getValue()) + "@HOME_SPLIT@";
        }
        return s;
    }

    public static HashMap<String, Location> convertStringToHomeMap(String s) {
        HashMap<String, Location> home_map = new HashMap<String, Location>();
        if (s.contains("@HOME_SPLIT@")) {
            for (String data : s.split("@HOME_SPLIT@")) {
                if (!data.contains("@")) continue;
                String hname = data.split("@")[0];
                Location hloc = LocationUtils.convertStringToLocation(data.split("@")[1]);
                home_map.put(hname, hloc);
            }
        }
        return home_map;
    }

    public static Location getNearbyLocation(Location l, int radius, int minDistance, int y_boost) {
        return LocationUtils.getNearbyLocation(l, radius, minDistance, y_boost, 0);
    }

    public static Location getNearbyLocation(Location l, int radius, int minDistance, int y_boost, int attempts) {
        boolean badMaterial;
        Random rand = new Random();
        Location rand_loc = l.clone();
        rand_loc.add((double)((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + minDistance)), 0.0, (double)((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + minDistance)));
        rand_loc.add(0.5, (double)y_boost, 0.5);
        Block b = rand_loc.getWorld().getHighestBlockAt(rand_loc).getLocation().subtract(0.0, 1.0, 0.0).getBlock();
        Material type = b.getType();
        boolean bl = badMaterial = b.isLiquid() || type == Material.STATIONARY_WATER || type == Material.WATER || type == Material.SNOW || type == Material.STEP || type == Material.WOOD_STEP || type == Material.BROWN_MUSHROOM || type == Material.RED_MUSHROOM;
        if (badMaterial) {
            if (attempts >= 150) {
                Bukkit.getLogger().info("Unable to find valid flare drop location: " + (Object)b + " started at: " + (Object)l);
                return null;
            }
            return LocationUtils.getNearbyLocation(l, radius, minDistance, y_boost, ++attempts);
        }
        while ((rand_loc.getBlock().getType() != Material.AIR || rand_loc.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) && rand_loc.getY() < 255.0) {
            rand_loc = rand_loc.add(0.0, 1.0, 0.0);
        }
        return rand_loc;
    }
}

