package net.astropvp.core.utils;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public class LocationUtils {
    public static Location setY(Location location, int y) {
        location.setY(location.getY() + y);
        return location;
    }

    public static Location setZ(Location location, int z) {
        location.setZ(location.getZ() + z);
        return location;
    }

    public static Location setX(Location location, int x) {
        location.setX(location.getX() + x);
        return location;
    }

    public static Location findHighestBlock(Location location) {
        if (location.getBlock().getType() == Material.AIR) {
            while (location.getBlock().getType() == Material.AIR && location.getY() > 0.0D)
                location.add(0.0D, -1.0D, 0.0D);
            return location;
        }
        if (location.getBlock().getType() != Material.AIR) {
            while (location.getBlock().getType() != Material.AIR && location.getY() < 256.0D)
                location.add(0.0D, 1.0D, 0.0D);
            location.add(0.0D, -1.0D, 0.0D);
        }
        return location;
    }

    public static String toStringLocation(Location location) {
        int x = (int) location.getX();
        int y = (int) location.getY();
        int z = (int) location.getZ();
        String world = location.getWorld().getName();
        String result = String.valueOf(world) + " " + x + " " + y + " " + z;
        return result;
    }

    public static Location fromStringLocation(String location) {
        String[] list = location.split(" ");
        World world = Bukkit.getWorld(list[0]);
        int x = Integer.valueOf(list[1]).intValue();
        int y = Integer.valueOf(list[2]).intValue();
        int z = Integer.valueOf(list[3]).intValue();
        return new Location(world, x, y, z);
    }

    public static Set<Player> getNearbyPlayers(Location location, double radius) {
        Set<Player> ret = Sets.newHashSet();
        double radiusSquared = radius * radius;
        for (Player player : location.getWorld().getPlayers()) {
            Location playerLocation = player.getLocation();
            double distanceSquared = location.distanceSquared(playerLocation);
            if (distanceSquared > radiusSquared)
                continue;
            ret.add(player);
        }
        return ret;
    }
}
