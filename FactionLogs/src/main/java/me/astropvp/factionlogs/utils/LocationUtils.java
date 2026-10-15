package me.astropvp.factionlogs.utils;

import org.bukkit.Location;

public class LocationUtils {
    public static String locToString(Location l) {
        return "[" + l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "]";
    }
}
