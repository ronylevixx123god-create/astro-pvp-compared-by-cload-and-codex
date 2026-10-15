package me.ifamasssxd.cosmicsky.inventorypets.utils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
    public static long formatSimpleTimeToMilliseconds(String timeStr) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("^(\\d+)([smhdw])$");
        Matcher matcher = pattern.matcher(timeStr);
        if (matcher.find()) {
            int firstNumber = Integer.parseInt(matcher.group(1));
            String multiplierStr = matcher.group(2);
            long multiplyBy = multiplierStr.equals("s") ? 1000L : (multiplierStr.equals("m") ? 60000L : (multiplierStr.equals("h") ? 3600000L : (multiplierStr.equals("d") ? 86400000L : 604800000L)));
            return firstNumber * multiplyBy;
        }
        throw new IllegalArgumentException();
    }

    public static String getRemaining(int seconds, TimeFormat type) {
        if (seconds < 60) {
            switch (type) {
                case DAYS:
                    return "0";
                case HOURS:
                    return "0";
                case MINUTES:
                    return "0";
                case SECONDS:
                    return String.valueOf(seconds);
            }
            return String.valueOf(seconds);
        }
        int minutes = seconds / 60;
        int s = 60 * minutes;
        int secondsLeft = seconds - s;
        if (minutes < 60) {
            switch (type) {
                case DAYS:
                    return "0";
                case HOURS:
                    return "0";
                case MINUTES:
                    return String.valueOf(minutes);
                case SECONDS:
                    return String.valueOf(secondsLeft);
            }
            return String.valueOf(seconds);
        }
        if (minutes < 1440) {
            int i = minutes / 60;
            int j = 60 * i;
            int k = minutes - j;
            switch (type) {
                case DAYS:
                    return "0";
                case HOURS:
                    return String.valueOf(i);
                case MINUTES:
                    return String.valueOf(k);
                case SECONDS:
                    return String.valueOf(secondsLeft);
            }
            return String.valueOf(seconds);
        }
        int days = minutes / 1440;
        int inMins = 1440 * days;
        int leftOver = minutes - inMins;
        if (leftOver < 60) {
            switch (type) {
                case DAYS:
                    return String.valueOf(days);
                case HOURS:
                    return String.valueOf(0);
                case MINUTES:
                    return String.valueOf(leftOver);
                case SECONDS:
                    return String.valueOf(secondsLeft);
            }
            return String.valueOf(seconds);
        }
        int hours = leftOver / 60;
        int hoursInMins = 60 * hours;
        int minsLeft = leftOver - hoursInMins;
        switch (type) {
            case DAYS:
                return String.valueOf(days);
            case HOURS:
                return String.valueOf(hours);
            case MINUTES:
                return String.valueOf(minsLeft);
            case SECONDS:
                return String.valueOf(secondsLeft);
        }
        return String.valueOf(seconds);
    }

    public static String getTime(int seconds) {
        if (seconds < 60)
            return seconds + "s";
        int minutes = seconds / 60;
        int s = 60 * minutes;
        int secondsLeft = seconds - s;
        if (minutes < 60)
            return (secondsLeft > 0) ? String.valueOf(minutes + "m " + secondsLeft + "s") : String.valueOf(minutes + "m");
        if (minutes < 1440) {
            int i = minutes / 60;
            String str = i + "h";
            int j = 60 * i;
            int k = minutes - j;
            if (k >= 1)
                str = str + " " + k + "m";
            if (secondsLeft > 0)
                str = str + " " + secondsLeft + "s";
            return str;
        }
        int days = minutes / 1440;
        String time = days + "d";
        int inMins = 1440 * days;
        int leftOver = minutes - inMins;
        if (leftOver >= 1)
            if (leftOver < 60) {
                time = time + " " + leftOver + "m";
            } else {
                int hours = leftOver / 60;
                time = time + " " + hours + "h";
                int hoursInMins = 60 * hours;
                int minsLeft = leftOver - hoursInMins;
                time = time + " " + minsLeft + "m";
            }
        if (secondsLeft > 0)
            time = time + " " + secondsLeft + "s";
        return time;
    }

    public static String formatTime(int secs) {
        int remainder = secs % 86400;
        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = remainder / 60 - hours * 60;
        int seconds = remainder % 3600 - minutes * 60;
        String fDays = (days > 0) ? (" " + days + " day" + ((days > 1) ? "s" : "")) : "";
        String fHours = (hours > 0) ? (" " + hours + " hour" + ((hours > 1) ? "s" : "")) : "";
        String fMinutes = (minutes > 0) ? (" " + minutes + " minute" + ((minutes > 1) ? "s" : "")) : "";
        String fSeconds = (seconds > 0) ? (" " + seconds + " second" + ((seconds > 1) ? "s" : "")) : "";
        String result = String.valueOf(fDays) + fHours + fMinutes + fSeconds;
        if (result.equals(""))
            return "0 seconds";
        return result.substring(1, result.length());
    }

    public static String formatTime(long secs) {
        secs /= 1000L;
        long remainder = secs % 86400L;
        long days = secs / 86400L;
        long hours = remainder / 3600L;
        long minutes = remainder / 60L - hours * 60L;
        long seconds = remainder % 3600L - minutes * 60L;
        String fDays = (days > 0L) ? (" " + days + " day" + ((days > 1L) ? "s" : "")) : "";
        String fHours = (hours > 0L) ? (" " + hours + " hour" + ((hours > 1L) ? "s" : "")) : "";
        String fMinutes = (minutes > 0L) ? (" " + minutes + " minute" + ((minutes > 1L) ? "s" : "")) : "";
        String fSeconds = (seconds > 0L) ? (" " + seconds + " second" + ((seconds > 1L) ? "s" : "")) : "";
        String result = String.valueOf(fDays) + fHours + fMinutes + fSeconds;
        if (result.equals(""))
            return " 0 seconds";
        return result;
    }

    public static String formatTimeAbbreviated(long seconds) {
        if (seconds == 0L)
            return "0s";
        long day = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - day * 24L;
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long secs = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
        StringBuilder sb = new StringBuilder();
        if (day > 0L)
            sb.append(day).append((day == 1L) ? "d" : "d").append(" ");
        if (hours > 0L)
            sb.append(hours).append((hours == 1L) ? "h" : "h").append(" ");
        if (minutes > 0L)
            sb.append(minutes).append((minutes == 1L) ? "m" : "m").append(" ");
        if (secs > 0L)
            sb.append(secs).append((secs == 1L) ? "s" : "s");
        String diff = sb.toString();
        return diff.isEmpty() ? "Now" : diff.trim();
    }

    public static String formatTime(long secs, boolean div) {
        long remainder = secs % 86400L;
        long days = secs / 86400L;
        long hours = remainder / 3600L;
        long minutes = remainder / 60L - hours * 60L;
        long seconds = remainder % 3600L - minutes * 60L;
        String fDays = (days > 0L) ? (" " + days + " day" + ((days > 1L) ? "s" : "")) : "";
        String fHours = (hours > 0L) ? (" " + hours + " hour" + ((hours > 1L) ? "s" : "")) : "";
        String fMinutes = (minutes > 0L) ? (" " + minutes + " minute" + ((minutes > 1L) ? "s" : "")) : "";
        String fSeconds = (seconds > 0L) ? (" " + seconds + " second" + ((seconds > 1L) ? "s" : "")) : "";
        String result = String.valueOf(fDays) + fHours + fMinutes + fSeconds;
        if (result.equals(""))
            return " 0 seconds";
        return result;
    }

    public enum TimeFormat {
        DAYS, HOURS, MINUTES, SECONDS;
    }
}