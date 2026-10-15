package me.ifamasssxd.cosmicbosses.struct;

import org.bukkit.Material;

public class NewBlockData {
    Material material;
    byte data;

    public NewBlockData(final Material material, final byte data) {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return this.material;
    }

    public byte getData() {
        return this.data;
    }
}
