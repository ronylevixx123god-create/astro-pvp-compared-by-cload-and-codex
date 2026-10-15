package me.ifamasssxd.cosmicbosses.struct.blockmanager;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BlockChangeManager {
    private ConcurrentHashMap<Location, StoredBlock> storedBlocks;
    public List<Material> validBreakables;
    private List<BlockFace> faces;
    private List<Material> flowers;
    Random random;

    public BlockChangeManager() {
        this.storedBlocks = new ConcurrentHashMap<Location, StoredBlock>();
        this.validBreakables = (List<Material>) Lists.newArrayList(new Material[]{Material.STONE, Material.GRASS, Material.COAL_BLOCK, Material.DIRT, Material.STAINED_CLAY, Material.ICE, Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.LOG, Material.LEAVES, Material.COBBLESTONE, Material.SMOOTH_BRICK, Material.CACTUS, Material.SANDSTONE, Material.SAND});
        this.faces = (List<BlockFace>) Lists.newArrayList(new BlockFace[]{BlockFace.EAST, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN});
        this.flowers = (List<Material>) Lists.newArrayList(new Material[]{Material.LONG_GRASS, Material.BROWN_MUSHROOM, Material.RED_ROSE, Material.RED_MUSHROOM, Material.YELLOW_FLOWER, Material.CACTUS, Material.SUGAR_CANE_BLOCK});
        this.random = new Random();
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) CosmicBosses.getInstance(), this::checkStoredBlocks, 200L, 200L);
    }

    private void checkStoredBlocks() {
        for (final Map.Entry<Location, StoredBlock> entry : this.storedBlocks.entrySet()) {
            final StoredBlock stored = entry.getValue();
            if (stored.getResetTime() != -1L && stored.getResetTime() <= System.currentTimeMillis()) {
                this.restoreBlock(entry.getKey());
            }
        }
    }

    public void restoreAllBlocks() {
        this.storedBlocks.keySet().forEach(this::restoreBlock);
    }

    public void restoreAllBlocks(final int bossId) {
        this.storedBlocks.entrySet().stream().filter(entry -> entry.getValue().getBossId() == bossId).forEach(entry -> this.restoreBlock(entry.getKey()));
    }

    public void createCustomExplosion(final Location location, final int radius, final int yRadius, final int bossId) {
        final int currentX = location.getBlockX();
        final int currentY = location.getBlockY();
        final int currentZ = location.getBlockZ();
        location.getWorld().playSound(location, Sound.EXPLODE, 1.0f, 1.0f);
        int destroyed = 0;
        final double r2 = radius * radius;
        final long start = System.currentTimeMillis();
        final Set<Block> blocks = new HashSet<Block>();
        for (int x = -radius; x <= radius; ++x) {
            for (int y = -yRadius; y <= yRadius; ++y) {
                for (int z = -radius; z <= radius; ++z) {
                    if (destroyed > 400) {
                        System.out.println("Too many blocks.");
                        return;
                    }
                    if (x * x + y * y + z * z + x + y + z + 0.75 <= r2) {
                        final Block block = location.getWorld().getBlockAt(currentX + x, currentY + y, currentZ + z);
                        if (block.getType().isSolid() && this.validBreakables.contains(block.getType())) {
                            if (!WorldGuardUtils.getRegionName(block.getLocation()).startsWith("spawn_")) {
                                ++destroyed;
                                blocks.add(block);
                            }
                        }
                    }
                }
            }
        }
        Label_0262:
        for (final Block block2 : blocks) {
            for (final BlockFace face : this.faces) {
                final Block nextTo = block2.getRelative(face);
                if (nextTo.getType() == Material.WATER || nextTo.getType() == Material.STATIONARY_WATER || nextTo.getType() == Material.LAVA) {
                    continue Label_0262;
                }
                if (nextTo.getType() == Material.STATIONARY_LAVA) {
                    continue Label_0262;
                }
                if (face == BlockFace.UP && this.flowers.contains(nextTo.getType())) {
                    continue Label_0262;
                }
            }
            this.destroyBlock(block2, bossId, 300, true);
        }
        System.out.println("Created explosion took " + (System.currentTimeMillis() - start) + "ms");
    }

    public void destroyBlock(final Block block, final int bossId, final int seconds, final boolean throwBlock) {
        if (block.getType() != Material.AIR) {
            final StoredBlock storedBlock = new StoredBlock(bossId, block.getType(), block.getData(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
            this.storedBlocks.put(block.getLocation(), storedBlock);
            block.setTypeIdAndData(0, (byte) 0, false);
            if (throwBlock && this.random.nextInt(10) > 1) {
                final FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), storedBlock.getMaterial(), storedBlock.getBlockData());
                fallingBlock.setDropItem(false);
                fallingBlock.setMetadata("boss_block", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
                final Vector velocity = new Vector(this.random.nextBoolean() ? (-this.random.nextDouble()) : this.random.nextDouble(), 1.0 + this.random.nextDouble(), this.random.nextBoolean() ? (-this.random.nextDouble()) : this.random.nextDouble()).normalize();
                fallingBlock.setVelocity(velocity);
            }
        }
    }

    public void replaceBlock(final Block block, final int bossId, final int seconds, final Material replace, final byte data) {
        final StoredBlock storedBlock = new StoredBlock(bossId, block.getType(), block.getData(), (seconds == -1) ? -1L : (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds)));
        this.storedBlocks.put(block.getLocation(), storedBlock);
        block.setTypeIdAndData(replace.getId(), data, false);
    }

    public void restoreBlock(final Location location, final StoredBlock block) {
        location.getBlock().setTypeIdAndData(block.getMaterial().getId(), block.getBlockData(), false);
    }

    public void restoreBlock(final Location location) {
        final StoredBlock block = this.storedBlocks.remove(location);
        if (block == null) {
            return;
        }
        location.getBlock().setTypeIdAndData(block.getMaterial().getId(), block.getBlockData(), false);
    }

    public ConcurrentHashMap<Location, StoredBlock> getStoredBlocks() {
        return this.storedBlocks;
    }
}
