/*
 * Decompiled with CFR 0.150.
 */
package net.astropvp.core.cosmicjackpot.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String formatDifference(long time) {
        String diff;
        if (time == 0L) {
            return "Never";
        }
        long day = TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time) - day * 24L;
        long minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.SECONDS.toHours(time) * 60L;
        long seconds = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.SECONDS.toMinutes(time) * 60L;
        StringBuilder sb = new StringBuilder();
        if (day > 0L) {
            sb.append(day).append("d").append(" ");
        }
        if (hours > 0L) {
            sb.append(hours).append("h").append(" ");
        }
        if (minutes > 0L) {
            sb.append(minutes).append("m").append(" ");
        }
        if (seconds > 0L) {
            sb.append(seconds).append("s");
        }
        return (diff = sb.toString()).isEmpty() ? "Now" : diff;
    }
}

