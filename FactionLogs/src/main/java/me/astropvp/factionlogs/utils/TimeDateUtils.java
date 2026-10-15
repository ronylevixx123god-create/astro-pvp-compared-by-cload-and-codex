package me.astropvp.factionlogs.utils;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeDateUtils {

    public static String splitToComponentTimes(long seconds) {
        int days = (int) seconds / 86400;

        int hours = (int) seconds / 3600;

        int remainder = (int) seconds - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        return days + "d " + hours + "h " + mins + "m " + secs + "s ";
    }

    public static String fancy(long seconds) {
        seconds = seconds / 1000;
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

        return day + "d " + hours + "h " + minute + "m " + second + "s ";

    }
}
