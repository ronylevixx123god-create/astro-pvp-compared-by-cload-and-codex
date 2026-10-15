/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicmysterychest.struct;

import org.bukkit.inventory.ItemStack;

public class CustomLoot {
    private ItemStack item;
    private double chance;
    private boolean jackpotItem;

    public CustomLoot(ItemStack item, double chance, boolean jackpotItem) {
        this.item = item;
        this.chance = chance;
        this.jackpotItem = jackpotItem;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public double getChance() {
        return this.chance;
    }

    public boolean isJackpotItem() {
        return this.jackpotItem;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CustomLoot)) {
            return false;
        }
        CustomLoot other = (CustomLoot) o;
        if (!other.canEqual(this)) {
            return false;
        }
        ItemStack this$item = this.getItem();
        ItemStack other$item = other.getItem();
        if (this$item == null ? other$item == null : this$item.equals((Object) other$item)) {
            return Double.compare(this.getChance(), other.getChance()) == 0 && this.isJackpotItem() == other.isJackpotItem();
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CustomLoot;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        ItemStack $item = this.getItem();
        result = result * 59 + ($item == null ? 43 : $item.hashCode());
        long $chance = Double.doubleToLongBits(this.getChance());
        result = result * 59 + (int) ($chance >>> 32 ^ $chance);
        result = result * 59 + (this.isJackpotItem() ? 79 : 97);
        return result;
    }
}

