package net.astropvp.customblocks.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChunkUtil {
    public static boolean isLocationAInSameChunkAsLocationB(Location aLocation, Location bLocation) {
        double aX = Math.floor(aLocation.getBlockX() >> 4);
        double aZ = Math.floor(aLocation.getBlockZ() >> 4);
        double bX = Math.floor(bLocation.getBlockX() >> 4);
        double bZ = Math.floor(bLocation.getBlockZ() >> 4);

        return aX == bX && aZ == bZ;

    }

    public static Chunk getIfLoaded(int chunkX, int chunkZ, World bWorld) {
        net.minecraft.server.v1_8_R3.World world = ((CraftWorld) bWorld).getHandle();
        net.minecraft.server.v1_8_R3.Chunk theChunk = world.getChunkIfLoaded(chunkX, chunkZ);
        return (theChunk == null) ? null : theChunk.bukkitChunk;
    }

    public static List<CraftEntity> getEntities(Chunk chunk) {
        net.minecraft.server.v1_8_R3.Chunk nmsChunk = ((CraftChunk) chunk).getHandle();
        List<CraftEntity> toReturn = new ArrayList<>();
        if (nmsChunk == null) return toReturn;
        for (List<net.minecraft.server.v1_8_R3.Entity> entities : nmsChunk.entitySlices) {
            for (net.minecraft.server.v1_8_R3.Entity entity : entities) {
                if (entity.getBukkitEntity() == null) continue;
                toReturn.add(entity.getBukkitEntity());
            }
        }
        return toReturn;
    }

    public static boolean isEmpty(Inventory inv) {
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }

    public static List<Location> getNearbyChests(Location location) {
        List<Location> chests = new ArrayList<>();

        if (location.clone().add(-1, 0, 0).getBlock().getType() == Material.CHEST || location.clone().add(-1, 0, 0).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(-1, 0, 0).getBlock().getState();
            chests.add(chest.getLocation());

        }
        if (location.clone().add(0, 0, -1).getBlock().getType() == Material.CHEST || location.clone().add(0, 0, -1).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(0, 0, -1).getBlock().getState();
            chests.add(chest.getLocation());

        }


        if (location.clone().add(1, 0, 0).getBlock().getType() == Material.CHEST || location.clone().add(1, 0, 0).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(1, 0, 0).getBlock().getState();
            chests.add(chest.getLocation());

        }

        if (location.clone().add(0, 0, 1).getBlock().getType() == Material.CHEST || location.clone().add(0, 0, 1).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(0, 0, 1).getBlock().getState();
            chests.add(chest.getLocation());

        }
        return chests;
    }

    public static Location getClosestChestLocation(Location location) {
        if (location.clone().add(-1, 0, 0).getBlock().getType() == Material.CHEST || location.clone().add(-1, 0, 0).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(-1, 0, 0).getBlock().getState();
            if (isEmpty(chest.getInventory())) {
                return location.clone().add(-1, 0, 0);
            }
        }
        if (location.clone().add(0, 0, -1).getBlock().getType() == Material.CHEST || location.clone().add(0, 0, -1).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(0, 0, -1).getBlock().getState();
            if (isEmpty(chest.getInventory())) {
                return location.clone().add(0, 0, -1);
            }
        }


        if (location.clone().add(1, 0, 0).getBlock().getType() == Material.CHEST || location.clone().add(1, 0, 0).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(1, 0, 0).getBlock().getState();
            if (isEmpty(chest.getInventory())) {
                return location.clone().add(1, 0, 0);
            }
        }

        if (location.clone().add(0, 0, 1).getBlock().getType() == Material.CHEST || location.clone().add(0, 0, 1).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(0, 0, 1).getBlock().getState();
            if (isEmpty(chest.getInventory())) {
                return location.clone().add(0, 0, 1);
            }
        }
        return null;

    }

    public static boolean hasNearbyPlayer(Location location, double radius) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius , radius, radius)) {
            if (entity instanceof Player) return true;
        }
        return  false;
    }
}
