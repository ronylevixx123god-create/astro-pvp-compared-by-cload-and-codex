/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.block.CreatureSpawner
 *  org.bukkit.craftbukkit.v1_7_R4.block.CraftBlock
 *  org.bukkit.entity.EntityType
 */
package org.arkhamnetwork.Arkkit.libs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class SpawnerLocation {
    private final int x;
    private final int y;
    private final int z;
    private final int chunkX;
    private final int chunkZ;
    private final String worldName;
    private final EntityType spawnerType;
    private final int spawnerCount;
    private Location bukkitLocation;
    private long placeTime;

    @Deprecated
    public SpawnerLocation(int x, int y, int z, String worldName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = this.x >> 4;
        this.chunkZ = this.z >> 4;
        this.worldName = worldName;
        this.spawnerType = EntityType.UNKNOWN;
        this.placeTime = 0L;
        this.spawnerCount = 1;
    }

    @Deprecated
    public SpawnerLocation(int x, int y, int z, String worldName, EntityType spawnerType) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = this.x >> 4;
        this.chunkZ = this.z >> 4;
        this.worldName = worldName;
        this.spawnerType = spawnerType;
        this.placeTime = 0L;
        this.spawnerCount = 1;
    }

    @Deprecated
    public SpawnerLocation(int x, int y, int z, String worldName, EntityType spawnerType, long placeTime) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = this.x >> 4;
        this.chunkZ = this.z >> 4;
        this.worldName = worldName;
        this.spawnerType = spawnerType;
        this.placeTime = placeTime;
        this.spawnerCount = 1;
    }

    public SpawnerLocation(int x, int y, int z, String worldName, EntityType spawnerType, long placeTime, int spawnerCount) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = this.x >> 4;
        this.chunkZ = this.z >> 4;
        this.worldName = worldName;
        this.spawnerType = spawnerType;
        this.placeTime = placeTime;
        this.spawnerCount = spawnerCount;
    }

    public SpawnerLocation(Block block) {
        CreatureSpawner cs = (CreatureSpawner) block.getState();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.chunkX = this.x >> 4;
        this.chunkZ = this.z >> 4;
        this.worldName = block.getWorld().getName();
        this.spawnerType = cs.getSpawnedType();
        this.placeTime = System.currentTimeMillis();
        this.spawnerCount = cs.getSpawnerCount();
    }

    public String string() {
        return this.worldName + "," + this.x + "," + this.y + "," + this.z + "," + this.spawnerType.name() + ":" + this.spawnerCount + "," + this.placeTime;
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

    public EntityType getSpawnerType() {
        return this.spawnerType;
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

    public int getSpawnerCount() {
        return this.spawnerCount;
    }
}

