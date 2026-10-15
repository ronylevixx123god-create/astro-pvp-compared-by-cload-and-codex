/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum PanelColor {
    RED("Red", ChatColor.RED, DyeColor.RED),
    GRAY("Gray", ChatColor.GRAY, DyeColor.SILVER),
    WHITE("White", ChatColor.WHITE, DyeColor.WHITE),
    YELLOW("Yellow", ChatColor.YELLOW, DyeColor.YELLOW),
    BLACK("Black", ChatColor.BLACK, DyeColor.BLACK),
    DARK_BLUE("Blue", ChatColor.BLUE, DyeColor.BLUE),
    DARK_GRAY("Dark Gray", ChatColor.DARK_GRAY, DyeColor.GRAY),
    DARK_GREEN("Green", ChatColor.DARK_GREEN, DyeColor.GREEN),
    LIGHT_BLUE("Light Blue", ChatColor.AQUA, DyeColor.LIGHT_BLUE),
    LIGHT_GREEN("Light Green", ChatColor.GREEN, DyeColor.LIME),
    PINK("Pink", ChatColor.LIGHT_PURPLE, DyeColor.PINK),
    ORANGE("Orange", ChatColor.GOLD, DyeColor.ORANGE),
    PURPLE("Purple", ChatColor.DARK_PURPLE, DyeColor.PURPLE);

    private String name;
    private ChatColor chatColor;
    private DyeColor dyeColor;

    private PanelColor(String name, ChatColor chatColor, DyeColor dyeColor) {
        this.name = name;
        this.chatColor = chatColor;
        this.dyeColor = dyeColor;
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getChatColor() {
        return this.chatColor;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }
}

