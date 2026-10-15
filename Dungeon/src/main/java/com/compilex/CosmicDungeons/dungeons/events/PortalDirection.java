/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.map.MapPalette
 */
package com.compilex.CosmicDungeons.dungeons.events;

import org.bukkit.map.MapPalette;

public enum PortalDirection {
    LEFT((byte)16, "R"),
    MIDDLE((byte) 28, "G"),
    RIGHT(MapPalette.matchColor((int)148, (int)0, (int)211), "P");

    public byte color;
    private String loreName;

    private PortalDirection(byte color, String loreName) {
        this.color = color;
        this.loreName = loreName;
    }

    public byte getColor() {
        return this.color;
    }

    public String getLoreName() {
        return this.loreName;
    }
}

