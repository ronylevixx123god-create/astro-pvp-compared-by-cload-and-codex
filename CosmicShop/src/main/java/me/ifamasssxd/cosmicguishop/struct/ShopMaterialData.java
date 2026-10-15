/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package me.ifamasssxd.cosmicguishop.struct;

import org.bukkit.Material;

public class ShopMaterialData {
    private Material type;
    private short data;
    private int sellCount = 1;

    public ShopMaterialData(Material type) {
        this(type, 1);
    }

    public ShopMaterialData(Material type, int count) {
        this.type = type;
        this.sellCount = count;
    }

    public ShopMaterialData(Material type, short data, int sellCount) {
        this.type = type;
        this.data = data;
        this.sellCount = sellCount;
    }

    public String toString() {
        return (Object) this.type + (this.data != 0 ? "@" + this.data : "") + (this.sellCount > 1 ? "#" + this.sellCount : "");
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ShopMaterialData)) {
            return false;
        }
        ShopMaterialData other = (ShopMaterialData) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Material this$type = this.getType();
        Material other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals((Object) other$type)) {
            return false;
        }
        if (this.getData() != other.getData()) {
            return false;
        }
        return this.getSellCount() == other.getSellCount();
    }

    protected boolean canEqual(Object other) {
        return other instanceof ShopMaterialData;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Material $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        result = result * 59 + this.getData();
        result = result * 59 + this.getSellCount();
        return result;
    }

    public Material getType() {
        return this.type;
    }

    public short getData() {
        return this.data;
    }

    public int getSellCount() {
        return this.sellCount;
    }
}

