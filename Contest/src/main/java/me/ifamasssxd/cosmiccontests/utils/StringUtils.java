/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package me.ifamasssxd.cosmiccontests.utils;

import org.bukkit.ChatColor;

public class StringUtils {
    public static final String centerText(String text) {
        int maxWidth = 80;
        int spaces = (int)Math.round(((double)maxWidth - 1.4 * (double)ChatColor.stripColor((String)text).length()) / 2.0);
        String complete = "";
        for (int i = 1; i <= spaces; ++i) {
            complete = complete + " ";
        }
        complete = complete + text;
        return complete;
    }

    public static String getFormattedNumber(int i) {
        int j = i % 10;
        int k = i % 100;
        if (j == 1 && k != 11) {
            return i + "st";
        }
        if (j == 2 && k != 12) {
            return i + "nd";
        }
        if (j == 3 && k != 13) {
            return i + "rd";
        }
        return i + "th";
    }
}

