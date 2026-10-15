/*
 * Decompiled with CFR 0.150.
 */
package me.ifamasssxd.cosmicguishop.struct;

public class ItemPrice {
    private double buyPrice;
    private double sellPrice;

    public ItemPrice(double buyPrice, double sellPrice) {
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public double getBuyPrice() {
        return this.buyPrice;
    }

    public double getSellPrice() {
        return this.sellPrice;
    }
}

