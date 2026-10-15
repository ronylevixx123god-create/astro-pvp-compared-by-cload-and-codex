/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 */
package net.astropvp.core.autobroadcast.struct;

import org.bukkit.ChatColor;

public enum BroadcastMessages {
    DISCORD(ChatColor.GREEN + "&7Make sure to join our discord at &f/discord&7."),
    STORE(ChatColor.GREEN + "&7Make sure to check the &f&l55% SALE&7 on the store &7(/buy)&7."),
    CLIENT(ChatColor.GREEN + "&7With the &fAstro Client&7 you will get 1.5x EXP."),
    SPAWNERSHOP(ChatColor.GREEN + "&7You can buy spawners in bulk using the command &f\"/spawnershop <amount>\"&7."),

    NEBULA(ChatColor.GREEN + "&7You can buy the &d&lNebula &7Rank on the &fStore &7(/buy)&7."),
    LBFILTER(ChatColor.GREEN + "&7Did you know that you can filter &fLootbox Loot &7with the command &f\"/lbfilter\"&7.");
    String message;

    private BroadcastMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

