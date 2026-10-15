/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package com.compilex.CosmicDungeons.utils;

import com.compilex.CosmicDungeons.tasks.HolidayPortalTask;
import com.compilex.CosmicDungeons.utils.Logger;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.bukkit.ChatColor;

public class TimeUtils {
    public static String formatDifference(long time) {
        String diff;
        if (time == 0L) {
            return "Never";
        }
        long day = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) - day * 24L;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60L;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toMinutes(time) * 60L;
        StringBuilder sb = new StringBuilder();
        if (day > 0L) {
            sb.append(day).append(" ").append(day == 1L ? "day" : "days").append(" ");
        }
        if (hours > 0L) {
            sb.append(hours).append(" ").append(hours == 1L ? "hour" : "hours").append(" ");
        }
        if (minutes > 0L) {
            sb.append(minutes).append(" ").append(minutes == 1L ? "minute" : "minutes").append(" ");
        }
        if (seconds > 0L) {
            sb.append(seconds).append(" ").append(seconds == 1L ? "second" : "seconds").append((Object)ChatColor.RESET + " ");
        }
        return (diff = sb.toString()).isEmpty() ? ">12 hours" : diff;
    }

    public static long getMilliUntilNextPublicPortal() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("CST"));
        if (Logger.debug) {
            c.add(11, 8);
        }
        int hour = c.get(11);
        long currentTime = c.getTimeInMillis();
        long closestEvent = -1L;
        Calendar cal = TimeUtils.getFreshCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("CST"));
        int next = -1;
        if (hour >= HolidayPortalTask.spawnHour7PM || hour < HolidayPortalTask.spawnHour9AM) {
            next = HolidayPortalTask.spawnHour9AM;
        } else if (hour >= HolidayPortalTask.spawnHour9AM && hour < HolidayPortalTask.spawnHour7PM) {
            next = HolidayPortalTask.spawnHour7PM;
        }
        cal.set(11, next);
        if (cal.before(c)) {
            cal.add(5, 1);
        }
        closestEvent = cal.getTimeInMillis();
        return closestEvent - currentTime;
    }

    private static Calendar getFreshCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c;
    }
}

