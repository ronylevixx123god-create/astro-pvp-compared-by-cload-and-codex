/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 */
package org.arkhamnetwork.Arkkit.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class PistonManager
        extends HashMap<String, HashSet<Location>> {
    private static final long serialVersionUID = -6754405085225360026L;

    public void addPiston(Block b) {
        Chunk c = b.getChunk();
        int cx = c.getX();
        int cz = c.getZ();
        if (this.containsKey(cx + "," + cz)) {
            HashSet hloc = this.get(cx + "," + cz);
            hloc.add(b.getLocation());
            this.put(cx + "," + cz, hloc);
        } else {
            HashSet<Location> hloc = new HashSet<Location>(Arrays.asList(b.getLocation()));
            this.put(cx + "," + cz, hloc);
        }
    }

    public void removePiston(Block b) {
        Chunk c = b.getChunk();
        int cx = c.getX();
        int cz = c.getZ();
        if (!this.containsKey(cx + "," + cz)) {
            return;
        }
        HashSet hloc = this.get(cx + "," + cz);
        hloc.remove(b.getLocation());
        this.put(cx + "," + cz, hloc);
    }

    public boolean containsPiston(Block b) {
        Chunk c = b.getChunk();
        int cx = c.getX();
        int cz = c.getZ();
        if (this.containsKey(cx + "," + cz)) {
            return this.get(cx + "," + cz).contains(b.getLocation());
        }
        return false;
    }

    public int getPistonCount(Chunk c) {
        int cx = c.getX();
        int cz = c.getZ();
        if (this.containsKey(cx + "," + cz)) {
            return this.get(cx + "," + cz).size();
        }
        return 0;
    }

    public boolean isAllowedPiston(Block b) {
        return this.containsPiston(b);
    }
}

