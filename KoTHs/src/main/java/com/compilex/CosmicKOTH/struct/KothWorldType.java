/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 */
package com.compilex.CosmicKOTH.struct;

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public enum KothWorldType {
    INFERNAL_ABYSS("&c", 1),
    MOUNT_OLYMPUS("&9", 3),
    COSMONAUT("&b", 2),
    ENDLESS_VOID("&5", 4),
    SANTA_WORKSHOP("&f", 5, "Santa's Workshop"),
    HALLOWEEN_NIGHTMARE("&6", 6);

    private String color;
    private String name;
    private int mapIndex;

    private KothWorldType(String color, int mapIndex) {
        this.color = ChatColor.translateAlternateColorCodes((char)'&', (String)color);
        this.name = StringUtils.capitaliseAllWords((String)this.name().toLowerCase().replace("_", " "));
        this.mapIndex = mapIndex;
    }

    private KothWorldType(String color, int mapIndex, String name) {
        this.color = ChatColor.translateAlternateColorCodes((char)'&', (String)color);
        this.name = name;
        this.mapIndex = mapIndex;
    }

    public static KothWorldType getFromIndex(int index) {
        return Arrays.stream(KothWorldType.values()).filter(k -> k.getMapIndex() == index).findFirst().orElse(null);
    }

    public String getName() {
        return this.getName(false);
    }

    public String getName(boolean withColor) {
        return (withColor ? this.color : "") + this.name;
    }

    public String getColor() {
        return this.color;
    }

    public int getMapIndex() {
        return this.mapIndex;
    }
}

