/*
 * Decompiled with CFR 0.150.
 */
package com.compilex.CosmicDungeons.struct;

import java.util.UUID;

public class PortalLog {
    private UUID placer;
    private String world;
    private int x;
    private int y;
    private int z;
    private long placetime;

    public PortalLog(UUID placer, String world, int x, int y, int z, long placetime) {
        this.placer = placer;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.placetime = placetime;
    }

    public UUID getPlacer() {
        return this.placer;
    }

    public String getWorld() {
        return this.world;
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

    public long getPlacetime() {
        return this.placetime;
    }
}

