package me.ifamasssxd.cosmicbosses.struct.enums;

public class ChunkCoords {
    int x;
    int z;

    public ChunkCoords(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setZ(final int z) {
        this.z = z;
    }
}
