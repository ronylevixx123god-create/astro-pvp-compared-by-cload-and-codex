package com.cosmicpvp.cosmicwildpvp;

import com.cosmicpvp.cosmicwildpvp.util.BlockModifier;
import com.cosmicpvp.cosmicwildpvp.util.BryUtil;
import com.google.common.collect.Maps;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Chunk;
import java.util.Map;

public class Cages
{
    public static Map<Chunk, Long> noClaimUntil;
    public BlockModifier blockModifier;
    private WildPvPPlugin plugin;

    public Cages(final WildPvPPlugin plugin) {
        this.plugin = plugin;
        this.blockModifier = new BlockModifier();
    }

    public void spawnCage(final Chunk chunk, final int y) {
        final int neg = -15;
        final int pos = 15;
        final int minX = -16;
        final int minZ = -16;
        final int maxX = 16;
        final int maxZ = 16;
        for (int x = -16; x < 16; ++x) {
            for (int z = -16; z < 16; ++z) {
                chunk.getBlock(x, y - 1, z);
                chunk.getBlock(x, y + 10, z);
                final Block[] array;
                final Block[] blocks = array = new Block[] { chunk.getBlock(x, y - 1, z), chunk.getBlock(x, y + 10, z) };
                for (final Block block : array) {
                    if (!this.blockModifier.containsLocation(chunk, block.getLocation()) && (block.getTypeId() == 175 || block.getTypeId() == 31 || block.getType() == Material.AIR || block.getType() == Material.LONG_GRASS || block.getType() == Material.DOUBLE_PLANT || block.getType() == Material.YELLOW_FLOWER || block.getType() == Material.RED_ROSE)) {
                        this.blockModifier.storeBlockToRemove(chunk, block.getLocation());
                        block.setType(Material.OBSIDIAN);
                    }
                }
            }
        }
        BryUtil.getChunkBorderLocations(chunk, y, 9).forEach(fence -> {
            if (fence.getBlock().getType() == Material.AIR || fence.getBlock().getType().isTransparent() || !fence.getBlock().getType().isSolid() || fence.getBlock().getType().name().contains("GRASS")) {
                this.blockModifier.storeBlockToRemove(chunk, fence);
                fence.getBlock().setType(Material.IRON_FENCE);
            }
            return;
        });
        Cages.noClaimUntil.put(chunk, System.currentTimeMillis() + 90000L);
    }

    public static boolean isChunkProtected(final Chunk chunk) {
        if (Cages.noClaimUntil.containsKey(chunk)) {
            final long expire = Cages.noClaimUntil.get(chunk);
            final long now = System.currentTimeMillis();
            return expire > now;
        }
        return false;
    }

    public void despawnCage(final Chunk chunk) {
        Cages.noClaimUntil.remove(chunk);
        if (this.blockModifier.containsChunk(chunk)) {
            this.blockModifier.removeBlocks(chunk, true, true);
        }
    }

    static {
        Cages.noClaimUntil = Maps.newHashMap();
    }
}
