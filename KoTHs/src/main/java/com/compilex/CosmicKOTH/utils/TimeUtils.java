/*
 * Decompiled with CFR 0.150.
 */
package com.compilex.CosmicKOTH.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String getSecondsAsString(long seconds) {
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (long)(day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
        if (day > 0) {
            return day + "d " + hours + "h " + minute + "m " + second + "s";
        }
        if (hours > 0L) {
            return hours + "h " + minute + "m " + second + "s";
        }
        if (minute > 0L) {
            return minute + "m " + second + "s";
        }
        return second + "s";
    }
}
