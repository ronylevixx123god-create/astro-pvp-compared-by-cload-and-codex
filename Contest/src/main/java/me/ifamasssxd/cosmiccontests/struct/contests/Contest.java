/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.material.MaterialData
 */
package me.ifamasssxd.cosmiccontests.struct.contests;

import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public abstract class Contest {
    private Material showMaterial;
    private short data;

    public Contest(Material material) {
        this.showMaterial = material;
    }

    public Contest(MaterialData material) {
        this.showMaterial = material.getItemType();
        this.data = material.getData();
    }

    public abstract String getName();

    public abstract String getFormattedData(ContestUser var1);

    public abstract String getFormattedSuffix(ContestUser var1);

    public abstract String getFormattedLine(ContestUser var1);

    public String getExtraSaveData() {
        return "";
    }

    public Material getShowMaterial() {
        return this.showMaterial;
    }

    public short getData() {
        return this.data;
    }

    public void setData(short data) {
        this.data = data;
    }
}

