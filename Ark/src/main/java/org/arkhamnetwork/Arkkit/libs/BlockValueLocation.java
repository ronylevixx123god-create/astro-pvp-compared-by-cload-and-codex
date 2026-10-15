/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 */
package org.arkhamnetwork.Arkkit.libs;

import org.arkhamnetwork.Arkkit.patches.BlockValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.concurrent.TimeUnit;

public class BlockValueLocation {
    private final int x;
    private final int y;
    private final int z;
    private final int chunkX;
    private final int chunkZ;
    private final String worldName;
    private final Material blockType;
    private Location bukkitLocation;
    private long placeTime;

    @Deprecated
    public BlockValueLocation(int x, int y, int z, String worldName, Material blockType) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = this.x >> 4;
        this.chunkZ = this.z >> 4;
        this.worldName = worldName;
        this.blockType = blockType;
        this.placeTime = 0L;
    }

    public BlockValueLocation(int x, int y, int z, String worldName, Material blockType, long placeTime) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = this.x >> 4;
        this.chunkZ = this.z >> 4;
        this.worldName = worldName;
        this.blockType = blockType;
        this.placeTime = placeTime;
    }

    public String string() {
        return this.worldName + "," + this.x + "," + this.y + "," + this.z + "," + this.blockType.name() + "," + this.placeTime;
    }

    public Location getLocation() {
        if (this.bukkitLocation == null && Bukkit.getWorld(this.worldName) != null) {
            this.bukkitLocation = new Location(Bukkit.getWorld(this.worldName), this.x, this.y, this.z);
        }
        return this.bukkitLocation;
    }

    public long getMillisecondsSincePlace() {
        return System.currentTimeMillis() - this.placeTime;
    }


    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public Material getBlockType() {
        return this.blockType;
    }

    public Location getBukkitLocation() {
        return this.bukkitLocation;
    }

    public long getPlaceTime() {
        return this.placeTime;
    }

    public void setPlaceTime(long placeTime) {
        this.placeTime = placeTime;
    }
}

