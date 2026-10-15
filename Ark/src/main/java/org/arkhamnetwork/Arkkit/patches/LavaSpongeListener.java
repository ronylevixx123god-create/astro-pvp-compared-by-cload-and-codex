/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.Block
 *  net.minecraft.server.v1_7_R4.Blocks
 *  net.minecraft.server.v1_7_R4.WorldServer
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockFromToEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;

final class LavaSpongeListener
        implements Listener {
    private final int radius = 3;

    LavaSpongeListener() {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpongeDestroy(BlockBreakEvent e) {
        org.bukkit.block.Block block = e.getBlock();
        World world = block.getWorld();
        WorldServer netWorld = ((CraftWorld) block.getWorld()).getHandle();
        int bx = block.getX();
        int by = block.getY();
        int bz = block.getZ();
        for (int fx = -this.radius; fx <= this.radius; ++fx) {
            for (int fy = -this.radius; fy <= this.radius; ++fy) {
                for (int fz = -this.radius; fz <= this.radius; ++fz) {
                    org.bukkit.block.Block b = world.getBlockAt(bx + fx, by + fy, bz + fz);
                    if (!b.getType().equals(Material.LAVA) && !b.getType().equals(Material.STATIONARY_LAVA))
                        continue;
                    netWorld.applyPhysics(new BlockPosition(b.getX(), b.getY(), b.getZ()), Blocks.LAVA);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWaterLavaUpdate(BlockFromToEvent e) {
        org.bukkit.block.Block block = e.getBlock();
        if (block.getType() != Material.WATER && block.getType() != Material.STATIONARY_WATER && block.getType() != Material.LAVA && block.getType() != Material.STATIONARY_LAVA) {
            return;
        }
        World world = block.getWorld();
        int bx = block.getX();
        int by = block.getY();
        int bz = block.getZ();
        for (int fx = -this.radius; fx <= this.radius; ++fx) {
            for (int fy = -this.radius; fy <= this.radius; ++fy) {
                for (int fz = -this.radius; fz <= this.radius; ++fz) {
                    org.bukkit.block.Block b = world.getBlockAt(bx + fx, by + fy, bz + fz);
                    if (!b.getType().equals(Material.SPONGE)) continue;
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        org.bukkit.block.Block block = e.getBlock();
        World world = block.getWorld();
        if (block.getType().equals(Material.SPONGE)) {
            int bx = block.getX();
            int by = block.getY();
            int bz = block.getZ();
            for (int fx = -this.radius; fx <= this.radius; ++fx) {
                for (int fy = -this.radius; fy <= this.radius; ++fy) {
                    for (int fz = -this.radius; fz <= this.radius; ++fz) {
                        org.bukkit.block.Block b = world.getBlockAt(bx + fx, by + fy, bz + fz);
                        if (!b.getType().equals(Material.LAVA) && !b.getType().equals(Material.STATIONARY_LAVA))
                            continue;
                        world.getBlockAt(bx + fx, by + fy, bz + fz).setType(Material.AIR);
                    }
                }
            }
        }
    }
}

