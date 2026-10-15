/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package com.compilex.CosmicDungeons.dungeons.libs;

import org.bukkit.ChatColor;

public enum DungeonState {
    EMPTY("&f&lEmpty", "&fno one is in the dungeon."),
    LOADING("&f&lLoading", "&fthere is an active portal to the dungeon."),
    IN_PROGRESS("&e&lIn Progress", "&ea group of players are running the dungeon."),
    COMPLETE("&a&lCleared", "&aa group of players just cleared the dungeon."),
    UNKNOWN("&7&lUnknown", "&7???");

    private String displayText;
    private String detailText;

    public String getDisplayText() {
        return this.displayText;
    }

    public String getDetailText() {
        return this.detailText;
    }

    private DungeonState(String displayText, String detailText) {
        this.displayText = ChatColor.translateAlternateColorCodes((char)'&', (String)displayText);
        this.detailText = ChatColor.translateAlternateColorCodes((char)'&', (String)detailText);
    }
}

