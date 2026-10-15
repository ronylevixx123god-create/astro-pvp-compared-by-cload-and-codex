package me.astropvp.astrospacegrinding.utils;

public class TimeUtils {

    public static String format(int seconds) {
        StringBuilder builder = new StringBuilder();
        int minutes = seconds / 60;
        seconds %= 60;
        int hours = minutes / 60;
        minutes %= 60;
        int days = hours / 24;
        hours %= 24;
        if (days > 0) {
            builder.append(days + "d, ");
        }
        if (hours > 0) {
            builder.append(hours + "h, ");
        }
        if (minutes > 0) {
            builder.append(minutes + "m, ");
        }
        if (seconds > 0 || builder.toString().length() == 0) {
            builder.append(seconds + "s, ");
        }
        return builder.toString().substring(0, builder.toString().lastIndexOf(","));
    }

    public static String formatMs(long ms) {
        StringBuilder builder = new StringBuilder();
        ms /=1000;
        int minutes = (int) (ms / 60);
        ms %= 60;
        int hours = minutes / 60;
        minutes %= 60;
        int days = hours / 24;
        hours %= 24;
        if (days > 0) {
            builder.append(days + "d, ");
        }
        if (hours > 0) {
            builder.append(hours + "h, ");
        }
        if (minutes > 0) {
            builder.append(minutes + "m, ");
        }
        if (ms > 0 || builder.toString().length() == 0) {
            builder.append(ms + "s, ");
        }
        return builder.toString().substring(0, builder.toString().lastIndexOf(","));
    }
}
