/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.utils;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockUtils {
    public static List<Block> getCube(Location loc, Integer radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (int x = radius * -1 - 1; x <= radius + 1; ++x) {
            for (int y = radius * -1; y <= radius; ++y) {
                for (int z = radius * -1 - 1; z <= radius + 1; ++z) {
                    Block b = loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
                    if (b.getType().equals((Object) Material.AIR) || b == null) continue;
                    blocks.add(b);
                }
            }
        }
        return blocks;
    }

    private static List<Block> getBlocks(Location base, int changeX, int changeY, int changeZ) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (int x = base.getBlockX() - changeX; x <= base.getBlockX() + changeX; ++x) {
            for (int y = base.getBlockY() - changeY; y <= base.getBlockY() + changeY; ++y) {
                for (int z = base.getBlockZ() - changeZ; z <= base.getBlockZ() + changeZ; ++z) {
                    Location loc = new Location(base.getWorld(), x, y, z);
                    Block b = loc.getBlock();
                    if (b.getType().equals((Object) Material.AIR) || b == null) continue;
                    blocks.add(b);
                }
            }
        }
        return blocks;
    }

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

    private static List<Block> getBlocksUpDown(Location base, int changeX, int changeY, int changeZ, int depthY) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        boolean depthPos = depthY >= 0;
        Block baseBlock = base.getBlock();
        for (int i = 0; i < Math.abs(depthY); ++i) {
            if (i == 0) {
                blocks.addAll(BlockUtils.getBlocks(baseBlock.getLocation(), changeX, changeY, changeZ));
                continue;
            }
            baseBlock = baseBlock.getLocation().add(0.0, depthPos ? 1.0 : -1.0, 0.0).getBlock();
            blocks.addAll(BlockUtils.getBlocks(baseBlock.getLocation(), changeX, changeY, changeZ));
        }
        return blocks;
    }

    private static List<Block> getBlocksWestEast(Location base, int changeX, int changeY, int changeZ, int depthX) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        boolean depthPos = depthX >= 0;
        Block baseBlock = base.getBlock();
        for (int i = 0; i < Math.abs(depthX); ++i) {
            if (i == 0) {
                blocks.addAll(BlockUtils.getBlocks(baseBlock.getLocation(), changeX, changeY, changeZ));
                continue;
            }
            baseBlock = baseBlock.getLocation().add(depthPos ? 1.0 : -1.0, 0.0, 0.0).getBlock();
            blocks.addAll(BlockUtils.getBlocks(baseBlock.getLocation(), changeX, changeY, changeZ));
        }
        return blocks;
    }

    private static List<Block> getBlocksNorthSouth(Location base, int changeX, int changeY, int changeZ, int depthZ) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        boolean depthPos = depthZ >= 0;
        Block baseBlock = base.getBlock();
        for (int i = 0; i < Math.abs(depthZ); ++i) {
            if (i == 0) {
                blocks.addAll(BlockUtils.getBlocks(baseBlock.getLocation(), changeX, changeY, changeZ));
                continue;
            }
            baseBlock = baseBlock.getLocation().add(0.0, 0.0, depthPos ? 1.0 : -1.0).getBlock();
            blocks.addAll(BlockUtils.getBlocks(baseBlock.getLocation(), changeX, changeY, changeZ));
        }
        return blocks;
    }

    public static List<Block> getSquareRaw(Block b, BlockFace face) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        switch (face) {
            case UP: {
                blocks.addAll(BlockUtils.getBlocks(b.getLocation(), 1, 0, 1));
                break;
            }
            case DOWN: {
                blocks.addAll(BlockUtils.getBlocks(b.getLocation(), 1, 0, 1));
                break;
            }
            case EAST: {
                blocks.addAll(BlockUtils.getBlocks(b.getLocation(), 0, 1, 1));
                break;
            }
            case WEST: {
                blocks.addAll(BlockUtils.getBlocks(b.getLocation(), 0, 1, 1));
                break;
            }
            case NORTH: {
                blocks.addAll(BlockUtils.getBlocks(b.getLocation(), 1, 1, 0));
                break;
            }
            case SOUTH: {
                blocks.addAll(BlockUtils.getBlocks(b.getLocation(), 1, 1, 0));
            }
        }
        return blocks;
    }

    public static Set<Block> getSquare(Block b, BlockFace face, int level) {
        HashSet<Block> blocks = new HashSet<Block>();
        int radius = 1;
        int depth = BlockUtils.getDepthByLevel(level);
        if (depth == 0) {
            return blocks;
        }
        switch (face) {
            case UP: {
                blocks.addAll(BlockUtils.getBlocksUpDown(b.getLocation(), radius, 0, radius, -depth));
                break;
            }
            case DOWN: {
                blocks.addAll(BlockUtils.getBlocksUpDown(b.getLocation(), radius, 0, radius, depth));
                break;
            }
            case EAST: {
                blocks.addAll(BlockUtils.getBlocksWestEast(b.getLocation(), 0, radius, radius, -depth));
                break;
            }
            case WEST: {
                blocks.addAll(BlockUtils.getBlocksWestEast(b.getLocation(), 0, radius, radius, depth));
                break;
            }
            case NORTH: {
                blocks.addAll(BlockUtils.getBlocksNorthSouth(b.getLocation(), radius, radius, 0, depth));
                break;
            }
            case SOUTH: {
                blocks.addAll(BlockUtils.getBlocksNorthSouth(b.getLocation(), radius, radius, 0, -depth));
            }
        }
        return blocks;
    }

    private static int getDepthByLevel(int level) {
        int depth = 0;
        if (level == 1 || level == 2) {
            if (Math.random() < (double) level * 0.33) {
                depth = 1;
            } else {
                return 0;
            }
        }
        if (level == 3) {
            depth = 1;
        }
        if (level == 4 || level == 5) {
            depth = Math.random() < (double) (level - 3) * 0.33 ? 2 : 1;
        }
        if (level == 6) {
            depth = 2;
        }
        if (level == 7 || level == 8) {
            depth = Math.random() < (double) (level - 6) * 0.33 ? 3 : 2;
        }
        if (level == 9) {
            depth = 3;
        }
        return depth;
    }
}

