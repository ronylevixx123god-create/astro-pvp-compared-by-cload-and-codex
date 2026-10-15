/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.dungeons.events.PortalDirection;
import com.compilex.CosmicDungeons.utils.LocationUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class PortalPuzzle {
    private Location spawnLocation;
    public Map<PortalDirection, List<Location>> portalBlocks;

    public PortalPuzzle(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        this.portalBlocks = new HashMap<PortalDirection, List<Location>>();
        Location portalLoc = this.spawnLocation.clone().add(12.0, 0.0, 0.0);
        portalLoc.setPitch(0.0f);
        portalLoc.setYaw(0.0f);
        this.portalBlocks.put(PortalDirection.LEFT, this.registerNearbyPortal(portalLoc.clone().add(0.0, 2.0, -5.0), 3, Material.PORTAL));
        this.portalBlocks.put(PortalDirection.MIDDLE, this.registerNearbyPortal(portalLoc.clone().add(1.0, 0.0, 0.0), 3, Material.PORTAL));
        this.portalBlocks.put(PortalDirection.RIGHT, this.registerNearbyPortal(portalLoc.clone().add(0.0, 2.0, 5.0), 3, Material.PORTAL));
    }

    public void updateLocations(World world) {
        this.spawnLocation.setWorld(world);
        this.portalBlocks.forEach((block, locs) -> locs.forEach(loc -> loc.setWorld(world)));
    }

    public PortalDirection findDirection(Location containingLocation) {
        for (Map.Entry<PortalDirection, List<Location>> entry : this.portalBlocks.entrySet()) {
            if (!entry.getValue().contains((Object)containingLocation)) continue;
            return entry.getKey();
        }
        return null;
    }

    private List<Location> registerNearbyPortal(Location center, int radius, Material type) {
        ArrayList loc = Lists.newArrayList();
        ArrayList list = Lists.newArrayList();
        LocationUtils.getNearbyBlocks(center, radius).forEach(l -> {
            if (l.getBlock().getType() == type) {
                loc.add(l);
            }
        });
        Bukkit.getLogger().info("Loaded " + loc.size() + " portal blocks near " + center.getX() + "x, " + center.getY() + "y, " + center.getZ() + "z");
        return loc;
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public Map<PortalDirection, List<Location>> getPortalBlocks() {
        return this.portalBlocks;
    }
}

