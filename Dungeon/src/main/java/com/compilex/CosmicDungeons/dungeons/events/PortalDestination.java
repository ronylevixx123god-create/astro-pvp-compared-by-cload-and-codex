/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.dungeons.events.PortalDirection;
import com.google.common.collect.Lists;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class PortalDestination {
    private List<Block> portalBlocks;
    private Location portalLocation;
    private PortalDirection portalDirection;

    public List<Block> getPortalBlocks() {
        return this.portalBlocks;
    }

    public Location getPortalLocation() {
        return this.portalLocation;
    }

    public PortalDirection getPortalDirection() {
        return this.portalDirection;
    }

    public PortalDestination(Location location, PortalDirection direction) {
        this.portalDirection = direction;
        this.portalLocation = location;
        this.portalBlocks = Lists.newArrayList();
        int radius = 3;
        Block at = location.getBlock();
        for (int x = -radius; x < radius; ++x) {
            for (int z = -radius; z < radius; ++z) {
                for (int y = -radius; y < radius; ++y) {
                    Block block = at.getRelative(x, y, z);
                    if (block.getType() != Material.PORTAL) continue;
                    this.portalBlocks.add(block);
                }
            }
        }
    }
}

