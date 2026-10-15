package me.astropvp.astrospawners.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

import java.util.ArrayList;
import java.util.List;

public class ChunkUtils {
    public static boolean isLocationAInSameChunkAsLocationB(Location aLocation, Location bLocation) {
        double aX = Math.floor(aLocation.getBlockX() >> 4);
        double aZ = Math.floor(aLocation.getBlockZ() >> 4);
        double bX = Math.floor(bLocation.getBlockX() >> 4);
        double bZ = Math.floor(bLocation.getBlockZ() >> 4);

        return aX == bX && aZ == bZ;

    }

    public static Chunk getChunkIfLoaded(int chunkX, int chunkZ, World bWorld) {
        net.minecraft.server.v1_8_R3.World world = ((CraftWorld) bWorld).getHandle();
        net.minecraft.server.v1_8_R3.Chunk theChunk = world.getChunkIfLoaded(chunkX, chunkZ);
        return (theChunk == null) ? null : theChunk.bukkitChunk;
    }

    public static List<CraftEntity> getEntitiesInChunk(Chunk chunk) {
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
}
