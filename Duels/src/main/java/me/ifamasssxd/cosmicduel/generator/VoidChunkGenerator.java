/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.generator.ChunkGenerator
 *  org.bukkit.generator.ChunkGenerator$BiomeGrid
 */
package me.ifamasssxd.cosmicduel.generator;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class VoidChunkGenerator
extends ChunkGenerator {
    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid) {
        byte[][] result = new byte[world.getMaxHeight() / 16][];
        if (chunkX == 0 && chunkZ == 0) {
            this.setBlock(result, 0, 64, 0, (byte)7);
        }
        return result;
    }

    void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][(y & 15) << 8 | z << 4 | x] = blkid;
    }
}

