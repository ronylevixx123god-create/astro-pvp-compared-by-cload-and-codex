/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.WorldServer
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 */
package me.ifamasssxd.cosmichcf.utils;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class BlockUtils {
    public static boolean isChunkLoaded(Block block) {
        WorldServer server = ((CraftWorld)block.getWorld()).getHandle();
        return server.isLoaded(new BlockPosition(block.getX(), block.getY(), block.getZ()));
    }
}

