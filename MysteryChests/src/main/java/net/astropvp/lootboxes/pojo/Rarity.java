/*
 * Decompiled with CFR 0.150.
 */
package net.astropvp.lootboxes.pojo;

public class Rarity {
    private final String id;
    private final String displayName;
    private final double chance;
    private final boolean isDefault;

    public Rarity(String id, String displayName, double chance, boolean isDefault) {
        this.id = id;
        this.displayName = displayName;
        this.chance = chance;
        this.isDefault = isDefault;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public double getChance() {
        return this.chance;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public String toString() {
        return "Rarity{id='" + this.id + '\'' + ", displayName='" + this.displayName + '\'' + ", chance=" + this.chance + ", isDefault=" + this.isDefault + '}';
    }
}

