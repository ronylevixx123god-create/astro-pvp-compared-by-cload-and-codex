package me.ifamasssxd.cosmicbosses.struct.blockmanager;

import org.bukkit.Material;

public class StoredBlock {
    int bossId;
    private Material material;
    private byte blockData;
    private long resetTime;

    public StoredBlock(final int bossId, final Material material, final byte blockData, final long resetTime) {
        this.bossId = -1;
        this.bossId = bossId;
        this.material = material;
        this.blockData = blockData;
        this.resetTime = resetTime;
    }

    public int getBossId() {
        return this.bossId;
    }

    public Material getMaterial() {
        return this.material;
    }

    public byte getBlockData() {
        return this.blockData;
    }

    public long getResetTime() {
        return this.resetTime;
    }
}
