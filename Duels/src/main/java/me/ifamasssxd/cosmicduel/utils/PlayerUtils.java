/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.Arkkit
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.util.Vector
 */
package me.ifamasssxd.cosmicduel.utils;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {
    static DecimalFormat df = new DecimalFormat("#,##0");

    public static int getRoundedSize(int size) {
        return size <= 9 ? 9 : (size <= 18 ? 18 : (size <= 27 ? 27 : (size <= 36 ? 36 : (size <= 45 ? 45 : (size <= 54 ? 54 : 54)))));
    }

    public static void addItem(Player p, ItemStack is) {
        if (is == null || is.getType() == Material.AIR) {
            return;
        }
        if (p.getInventory().firstEmpty() == -1) {
            if (p.getEnderChest().firstEmpty() == -1) {
                p.sendMessage((Object)ChatColor.YELLOW + "Item dropped on the ground.");
                p.getWorld().dropItem(p.getLocation(), is);
            } else {
                p.sendMessage((Object)ChatColor.YELLOW + "Item added to your Ender Chest.");
                p.getEnderChest().addItem(new ItemStack[]{is});
            }
        } else {
            p.getInventory().addItem(new ItemStack[]{is});
        }
    }

    public static boolean isInventoryEmpty(Player player) {
        for (ItemStack is : player.getInventory()) {
            if (is == null || is.getType() == Material.AIR) continue;
            return false;
        }
        return true;
    }

    public static boolean inCombat(Player pl) {
        long tagDuration = Bukkit.getPluginManager().isPluginEnabled("Arkkit") ? Arkkit.get().getConfig().getLong("settings.combat_log.combat_duration") : 10000L;
        return pl.hasMetadata("cl_combat") && pl.getMetadata("cl_combat").size() > 0 && System.currentTimeMillis() - ((MetadataValue)pl.getMetadata("cl_combat").get(0)).asLong() <= tagDuration;
    }

    public static double getHealth(Player p) {
        return ((CraftPlayer)p).getHealth();
    }

    public static double getMaxHealth(Player p) {
        return ((CraftPlayer)p).getMaxHealth();
    }

    public static void setCollides(Player p, boolean val) {
        CraftPlayer cp = (CraftPlayer)p;
        cp.getHandle().collidesWithEntities = val;
        cp.getHandle().k = val;
    }

    public static int getEmptySlots(Player p) {
        int empty = 0;
        for (ItemStack is : p.getInventory()) {
            if (is != null && is.getType() != Material.AIR) continue;
            ++empty;
        }
        return empty;
    }

    public static String insertCommas(double value) {
        return df.format(value);
    }

    public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {
        double gravity = 0.115;
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(PlayerUtils.distanceSquared(from, to));
        double maxGain = heightGain > endGain + heightGain ? (double)heightGain : (double)(endGain + heightGain);
        double a = -horizDist * horizDist / (4.0 * maxGain);
        double b = horizDist;
        double c = -endGain;
        double slope = -b / (2.0 * a) - Math.sqrt(b * b - 4.0 * a * c) / (2.0 * a);
        double vy = Math.sqrt(maxGain * 0.115);
        double vh = vy / slope;
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = (double)dx / mag;
        double dirz = (double)dz / mag;
        double vx = vh * dirx;
        double vz = vh * dirz;
        return new Vector(vx, vy, vz);
    }

    private static double distanceSquared(Vector from, Vector to) {
        double dx = to.getBlockX() - from.getBlockX();
        double dz = to.getBlockZ() - from.getBlockZ();
        return dx * dx + dz * dz;
    }

    public static List<Location> getArch(Player p1, Player p2) {
        Location location = p1.getEyeLocation();
        Location target = p2.getEyeLocation();
        if (target == null) {
            return null;
        }
        int height = 2;
        int particles = 100;
        ArrayList<Location> locations = new ArrayList<Location>();
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float)link.length();
        float pitch = (float)(8.0 / Math.pow(length, 2.0));
        for (int i = 0; i < 100; ++i) {
            Vector v = link.clone().normalize().multiply(length * (float)i / 100.0f);
            float x = (float)(i / 100) * length - length / 2.0f;
            float y = (float)((double)(-pitch) * Math.pow(x, 2.0) + 2.0);
            location.add(v).add(0.0, (double)y, 0.0);
            locations.add(location.clone());
            location.subtract(0.0, (double)y, 0.0).subtract(v);
        }
        return locations;
    }
}

