/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 */
package org.arkhamnetwork.Arkkit.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public class ColorUtils {
    public static final ChatColor getChatColor(DyeColor dc) {
        if (dc == DyeColor.BLACK) {
            return ChatColor.BLACK;
        }
        if (dc == DyeColor.BLUE) {
            return ChatColor.BLUE;
        }
        if (dc == DyeColor.BROWN) {
            return ChatColor.GOLD;
        }
        if (dc == DyeColor.CYAN) {
            return ChatColor.AQUA;
        }
        if (dc == DyeColor.LIGHT_BLUE) {
            return ChatColor.DARK_AQUA;
        }
        if (dc == DyeColor.LIME) {
            return ChatColor.GREEN;
        }
        if (dc == DyeColor.PINK) {
            return ChatColor.LIGHT_PURPLE;
        }
        if (dc == DyeColor.PURPLE) {
            return ChatColor.DARK_PURPLE;
        }
        if (dc == DyeColor.RED) {
            return ChatColor.RED;
        }
        if (dc == DyeColor.SILVER) {
            return ChatColor.DARK_GRAY;
        }
        if (dc == DyeColor.YELLOW) {
            return ChatColor.YELLOW;
        }
        if (ChatColor.valueOf(dc.toString()) != null) {
            return ChatColor.valueOf(dc.toString());
        }
        return ChatColor.WHITE;
    }
}

