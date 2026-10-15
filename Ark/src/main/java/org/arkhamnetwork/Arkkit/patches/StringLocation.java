/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.block.Block
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.block.Block;

class StringLocation {
    String world;
    int x;
    int y;
    int z;

    StringLocation(Block b) {
        this.world = b.getWorld().getName();
        this.x = b.getX();
        this.y = b.getY();
        this.z = b.getZ();
    }

    StringLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String string() {
        return this.world + "," + this.x + "," + this.y + "," + this.z;
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
}

