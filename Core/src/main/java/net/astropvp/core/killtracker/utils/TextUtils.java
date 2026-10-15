/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.astropvp.core.killtracker.utils;

import java.util.concurrent.TimeUnit;
import org.bukkit.ChatColor;

public class TextUtils {
    public static String getFormattedCooldown(long time) {
        return TextUtils.getCooldownString((int)(time - System.currentTimeMillis()) / 1000);
    }

    public static String getCooldownString(int seconds) {
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (long)(day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
        String s = "";
        if (day != 0) {
            s = s + day + "d ";
        }
        if (hours != 0L) {
            s = s + hours + "h ";
        }
        if (minute != 0L) {
            s = s + minute + "m ";
        }
        if (second != 0L) {
            s = s + second + "s ";
        }
        if (s.isEmpty()) {
            return seconds + "s";
        }
        return s.trim();
    }

    public static String getProgressBar(int lines, int current, int needed) {
        StringBuilder builder = new StringBuilder();
        lines = 30;
        double percentage = Double.valueOf(current) / Double.valueOf(needed);
        int goodLines = (int)Math.floor(Double.valueOf(lines) * percentage);
        builder.append(ChatColor.GREEN.toString());
        for (int i = 0; i < goodLines; ++i) {
            builder.append("|");
        }
        int redLines = lines - goodLines;
        if (redLines > 0) {
            builder.append(ChatColor.RED.toString());
            for (int i = 0; i < redLines; ++i) {
                builder.append("|");
            }
        }
        return builder.toString();
    }
}

