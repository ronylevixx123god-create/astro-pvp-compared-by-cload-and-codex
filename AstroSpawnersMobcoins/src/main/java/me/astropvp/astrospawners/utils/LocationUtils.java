package me.astropvp.astrospawners.utils;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

public class LocationUtils {
    public static Location setY(Location location, double y) {
        location.setY(location.getY() + y);
        return location;
    }

    public static Location setZ(Location location, double z) {
        location.setZ(location.getZ() + z);
        return location;
    }

    public static Location setX(Location location, double x) {
        location.setX(location.getX() + x);
        return location;
    }

    public static Location findHighestBlock(Location location) {
        if (location.getBlock().getType() == Material.AIR) {
            while (location.getBlock().getType() == Material.AIR && location.getY() > 0) {
                location.add(0, -1, 0);
            }
            return location;
        }
        if (location.getBlock().getType() != Material.AIR) {
            while (location.getBlock().getType() != Material.AIR && location.getY() < 256) {
                location.add(0, 1, 0);
            }
            location.add(0, -1, 0);
        }
        return location;
    }

    public static String toStringLocation(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();
        String world = location.getWorld().getName();
        String result = world + " " + x + " " + y + " " + z + " " + pitch + " " + yaw;
        return result;
    }

    public static Location fromStringLocation(String location) {
        String[] list = location.split(" ");
        World world = Bukkit.getWorld(list[0]);
        double x = Double.valueOf(list[1]);
        double y = Double.valueOf(list[2]);
        double z = Double.valueOf(list[3]);
        float pitch = Float.valueOf(list[4]);
        float yaw = Float.valueOf(list[5]);
        Location loc = new Location(world, x, y, z);
        loc.setPitch(pitch);
        loc.setYaw(yaw);
        return loc;
    }

    public static Set<Player> getNearbyPlayers(Location location, double radius) {
        Set<Player> ret = Sets.newHashSet();
        final double radiusSquared = radius * radius;
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
