package net.astropvp.astroevents.utils;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public final class BlockUtil {

    private final static IBlockData AIR = Blocks.AIR.getBlockData();

    public static void setBlockFast(Block b, int blockId, byte data, boolean physics) {
        setBlockFast(b.getWorld(), b.getX(), b.getY(), b.getZ(), blockId, data, physics);
    }

    public static void setBlockFast(org.bukkit.World world, int x, int y, int z, int blockId, byte data, boolean physics) {
        net.minecraft.server.v1_8_R3.World w = ((CraftWorld) world).getHandle();
        BlockPosition bp = new BlockPosition(x, y, z);
        IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(blockId + (data << 12));
        w.setTypeAndData(bp, ibd, physics ? 3 : 2);
        (w.getChunkAt(x >> 4, z >> 4)).a(bp, ibd);
    }

    public static void setAir(Block b, boolean physics) {
        setAir(b.getWorld(), b.getX(), b.getY(), b.getZ(), physics);
    }

    public static void setAir(org.bukkit.World world, int x, int y, int z, boolean physics) {
        BlockPosition position = new BlockPosition(x, y, z);
        ((CraftWorld) world).getHandle().setTypeAndData(position, AIR, physics ? 3 : 2);
    }

    public static void updatePhysics(org.bukkit.World world, int x, int y, int z) {
        net.minecraft.server.v1_8_R3.World w = ((CraftWorld) world).getHandle();
        BlockPosition bp = new BlockPosition(x, y, z);
        w.applyPhysics(bp, w.getType(bp).getBlock());
    }


}
