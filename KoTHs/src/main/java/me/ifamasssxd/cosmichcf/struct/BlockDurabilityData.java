/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.material.MaterialData
 */
package me.ifamasssxd.cosmichcf.struct;

import org.bukkit.material.MaterialData;

public class BlockDurabilityData {
    private MaterialData blockType;
    private short durability;

    public BlockDurabilityData(MaterialData blockType, short durability) {
        this.blockType = blockType;
        this.durability = durability;
    }

    public MaterialData getBlockType() {
        return this.blockType;
    }

    public short getDurability() {
        return this.durability;
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }
}

