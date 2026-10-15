/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package com.compilex.CosmicKOTH.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ScoreboardUtils {
    private static final String scoreboardTitle = ChatColor.translateAlternateColorCodes((char)'&', (String)" &a&l&nAstro&r &e&l&nKOTH&r ");

    public static boolean updateScoreboard(Player player) throws ArrayIndexOutOfBoundsException, Exception {
        return true;
    }

    private static String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }
}

