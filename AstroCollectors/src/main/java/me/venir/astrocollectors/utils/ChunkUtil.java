package me.venir.astrocollectors.utils;

import org.bukkit.Location;

public class ChunkUtil {
    public static boolean isLocationAInSameChunkAsLocationB(Location aLocation, Location bLocation) {
        double aX = Math.floor(aLocation.getBlockX() / 16);
        double aZ = Math.floor(aLocation.getBlockZ() / 16);
        double bX = Math.floor(bLocation.getBlockX() / 16);
        double bZ = Math.floor(bLocation.getBlockZ() / 16);

        return aX == bX && aZ == bZ;

    }

}
