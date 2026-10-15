/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package com.compilex.CosmicDungeons.dungeons.libs;

import org.bukkit.ChatColor;

public enum DungeonEndReason {
    ALL_PLAYERS_DEAD(ChatColor.RED, "All players have died."),
    TIME_EXPIRE(ChatColor.RED, "The objective was not cleared fast enough."),
    DUNGEON_CLEARED(ChatColor.GREEN, "The dungeon is resetting."),
    UNKNOWN(ChatColor.GRAY, "???");

    private String detailText;
    private ChatColor detailColor;

    public String getDetailText() {
        return this.detailText;
    }

    public ChatColor getDetailColor() {
        return this.detailColor;
    }

    private DungeonEndReason(ChatColor detailColor, String detailText) {
        this.detailColor = detailColor;
        this.detailText = (Object)this.detailColor + ChatColor.translateAlternateColorCodes((char)'&', (String)detailText);
    }
}

