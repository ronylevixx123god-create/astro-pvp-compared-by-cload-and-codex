/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.CosmicSpaceChests.libs;

public enum ItemRarity {
    UNKNOWN("Unknown"),
    SIMPLE("Simple"),
    UNIQUE("Unique"),
    ELITE("Elite"),
    ULTIMATE("Ultimate"),
    LEGENDARY("Legendary"),
    GOD("Godly"),
    SOUL("Soul");

    public String name;

    private ItemRarity(String s) {
        this.name = s;
    }
}

