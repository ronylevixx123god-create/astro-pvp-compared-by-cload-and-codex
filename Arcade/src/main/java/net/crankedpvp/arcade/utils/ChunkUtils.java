package net.crankedpvp.arcade.utils;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.crankedpvp.arcade.ArcadeAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkUtils {
    public static List<Location> getChunkBorderLocations(Location loc, int height) {
        List<Location> blocks = Lists.newArrayList();
        Chunk c = loc.getChunk();
        int maxX = 15;
        int maxY = loc.getBlockY() + height;
        int maxZ = 15;
        int minX = -16;
        int minY = loc.getBlockY() - height;
        int minZ = -16;

        for (int locX = minX; locX <= maxX; ++locX) {
            for (int locZ = minZ; locZ <= maxZ; ++locZ) {
                for (int locY = minY; locY <= maxY; ++locY) {
                    Block block = c.getBlock(locX, locY, locZ);
                    if (locX == minX || locX == maxX || locZ == minZ || locZ == maxZ) {
                        blocks.add(block.getLocation());
                    }
                }
            }
        }
        return blocks;
    }

    public static void unHideBlocks(UUID uuid, List<Location> blocks) {
        Player player  = Bukkit.getPlayer(uuid);
        if (player == null || !player.isOnline()) return;
        for (Location location : blocks) {
            player.sendBlockChange(location, Material.AIR, (byte) 0);
        }
    }
    public static List<Location> getRegionBlocks(String region) {
        List<Location> blocks = new ArrayList<>();
        World bukkitWorld = Bukkit.getWorld(ArcadeAPI.WORLD_NAME);
        com.sk89q.worldedit.world.World weWorld = new BukkitWorld(bukkitWorld);
        ProtectedRegion wgRegion = WorldGuardUtils.getWorldGuard().getGlobalRegionManager().get(bukkitWorld).getRegion(region);
        Polygonal2DRegion weRegion = new Polygonal2DRegion(weWorld, wgRegion.getPoints(), wgRegion.getMinimumPoint().getBlockY(), wgRegion.getMaximumPoint().getBlockY());
        for (BlockVector block : weRegion) {
            blocks.add(new Location(bukkitWorld, block.getBlockX(), block.getBlockY(), block.getBlockZ()));
        }
        return blocks;
    }
}
