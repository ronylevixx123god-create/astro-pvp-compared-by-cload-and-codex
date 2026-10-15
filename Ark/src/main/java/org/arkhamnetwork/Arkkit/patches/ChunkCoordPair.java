/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.apache.commons.lang.builder.HashCodeBuilder
 */
package org.arkhamnetwork.Arkkit.patches;

import org.apache.commons.lang.builder.HashCodeBuilder;

class ChunkCoordPair {
    private final int x;
    private final int z;

    public ChunkCoordPair(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public boolean equals(Object obj) {
        ChunkCoordPair other;
        return obj instanceof ChunkCoordPair && (other = (ChunkCoordPair) obj).getX() == this.x && other.getZ() == this.z;
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.x).append(this.z).toHashCode();
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}

