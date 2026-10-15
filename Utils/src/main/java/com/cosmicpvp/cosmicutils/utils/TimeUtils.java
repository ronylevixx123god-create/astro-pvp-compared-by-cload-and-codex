/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String formatFutureTime(long timeInFuture) {
        long timeDifference = timeInFuture - System.currentTimeMillis();
        long seconds = timeDifference / 1000L;
        return TimeUtils.formatSeconds(seconds);
    }

    public static String formatSeconds(long seconds) {
        String diff;
        if (seconds == 0L) {
            return "0s";
        }
        long day = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - day * 24L;
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long secs = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
        StringBuilder sb = new StringBuilder();
        if (day > 0L) {
            sb.append(day).append(day == 1L ? "d" : "d").append(" ");
        }
        if (hours > 0L) {
            sb.append(hours).append(hours == 1L ? "h" : "h").append(" ");
        }
        if (minutes > 0L) {
            sb.append(minutes).append(minutes == 1L ? "m" : "m").append(" ");
        }
        if (secs > 0L) {
            sb.append(secs).append(secs == 1L ? "s" : "s");
        }
        return (diff = sb.toString()).isEmpty() ? "Now" : diff.trim();
    }

    public static String formatTimeToDate(long time, String format, String timeZone) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String dayNumberSuffix = TimeUtils.getDayNumberSuffix(cal.get(5));
        SimpleDateFormat dateFormat = new SimpleDateFormat(String.format(format, dayNumberSuffix));
        if (timeZone != null && !dateFormat.getTimeZone().getID().equals(timeZone)) {
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        return dateFormat.format(cal.getTime());
    }

    private static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1: {
                return "st";
            }
            case 2: {
                return "nd";
            }
            case 3: {
                return "rd";
            }
        }
        return "th";
    }
}

