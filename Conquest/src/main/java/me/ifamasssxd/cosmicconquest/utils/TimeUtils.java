// 
// Decompiled by Procyon v0.5.36
// 

package me.ifamasssxd.cosmicconquest.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils
{
    public static String formatDifference(final long time) {
        if (time == 0L) {
            return "Never";
        }
        final long day = TimeUnit.SECONDS.toDays(time);
        final long hours = TimeUnit.SECONDS.toHours(time) - day * 24L;
        final long minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.SECONDS.toHours(time) * 60L;
        final long seconds = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.SECONDS.toMinutes(time) * 60L;
        final StringBuilder sb = new StringBuilder();
        if (day > 0L) {
            sb.append(day).append(" ").append((day == 1L) ? "day" : "days").append(" ");
        }
        if (hours > 0L) {
            sb.append(hours).append(" ").append((hours == 1L) ? "hour" : "hours").append(" ");
        }
        if (minutes > 0L) {
            sb.append(minutes).append(" ").append((minutes == 1L) ? "minute" : "minutes").append(" ");
        }
        if (seconds > 0L && sb.length() <= 0) {
            sb.append(seconds).append(" ").append((seconds == 1L) ? "second" : "seconds");
        }
        final String diff = sb.toString();
        return diff.isEmpty() ? "Now" : diff;
    }
}
