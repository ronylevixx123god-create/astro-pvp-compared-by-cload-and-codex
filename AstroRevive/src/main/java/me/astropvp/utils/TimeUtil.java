package me.astropvp.utils;

public class TimeUtil {
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
        return fDays + fHours + fMinutes + fSeconds;
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
        String result = fDays + fHours + fMinutes + fSeconds;
        if (result.equals(""))
            return "0 seconds";
        return result;
    }
}

