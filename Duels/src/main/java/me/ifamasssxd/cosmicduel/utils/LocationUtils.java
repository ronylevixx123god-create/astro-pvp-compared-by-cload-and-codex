/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.WorldCreator
 *  org.bukkit.generator.ChunkGenerator
 */
package me.ifamasssxd.cosmicduel.utils;

import java.util.ArrayList;
import java.util.List;
import me.ifamasssxd.cosmicduel.generator.VoidChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

public class LocationUtils {
    public static Location getLocationFromString(String s, boolean loadWorld) {
        if (!s.contains(":")) {
            return null;
        }
        String[] args = s.split(":");
        if (args.length == 6) {
            String worldName = args[0];
            World world = Bukkit.getWorld((String)worldName);
            if (world == null && loadWorld) {
                world = Bukkit.createWorld((WorldCreator)new WorldCreator(worldName).generator((ChunkGenerator)new VoidChunkGenerator()).generateStructures(false));
            }
            return new Location(world, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
        }
        if (args.length == 4) {
            String worldName = args[0];
            World world = Bukkit.getWorld((String)worldName);
            if (world == null && loadWorld) {
                world = Bukkit.createWorld((WorldCreator)new WorldCreator(worldName).generator((ChunkGenerator)new VoidChunkGenerator()).generateStructures(false));
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

