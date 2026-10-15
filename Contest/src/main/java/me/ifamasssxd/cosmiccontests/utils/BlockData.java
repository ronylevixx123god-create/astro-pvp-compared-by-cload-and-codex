/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package me.ifamasssxd.cosmiccontests.utils;

import org.bukkit.Material;

public class BlockData {
    private Material material;
    private short data;

    public BlockData(Material material, short data) {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return this.material;
    }

    public short getData() {
        return this.data;
    }
}

