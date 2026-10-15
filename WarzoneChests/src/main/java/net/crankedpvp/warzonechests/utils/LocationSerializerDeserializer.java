package net.crankedpvp.warzonechests.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializerDeserializer {

    public static String serialize(Location obj) {
        return obj == null ? "" : obj.getWorld().getName() + ":" + obj.getBlockX() + ":" + obj.getBlockY() + ":" + obj.getBlockZ();
    }

    public static Location deserialize(String s) {
        if (s != null && !s.trim().equals("")) {
            String[] parts = s.split(":");
            if (parts.length == 4) {
                World w = Bukkit.getServer().getWorld(parts[0]);
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int z = Integer.parseInt(parts[3]);
                return new Location(w, (double)x, (double)y, (double)z);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
