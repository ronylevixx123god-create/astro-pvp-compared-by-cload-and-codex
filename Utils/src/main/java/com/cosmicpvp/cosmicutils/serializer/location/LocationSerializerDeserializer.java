package com.cosmicpvp.cosmicutils.serializer.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializerDeserializer {

    public static String serialize(Location obj) {
        if (obj == null) {
            return "";
        }
        return obj.getWorld().getName() + ":" + obj.getBlockX() + ":" + obj.getBlockY() + ":" + obj.getBlockZ();
    }

    public static Location deserialize(String s) {
        if (s == null || s.trim().equals("")) {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }
}
