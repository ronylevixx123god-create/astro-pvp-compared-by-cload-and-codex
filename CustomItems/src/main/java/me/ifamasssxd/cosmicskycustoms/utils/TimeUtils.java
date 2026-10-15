package me.ifamasssxd.cosmicskycustoms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String formatCurrentTimeMillis() {
       return formatTimeMillis(System.currentTimeMillis());
    }

    private static String addDaySuffix(String dateStr) {
        // Extract the day from the formatted date
        String[] parts = dateStr.split(" ");
        String day = parts[0];
        String suffix;

        // Determine the correct suffix
        switch (day) {
            case "1":
            case "21":
            case "31":
                suffix = "st";
                break;
            case "2":
            case "22":
                suffix = "nd";
                break;
            case "3":
            case "23":
                suffix = "rd";
                break;
            default:
                suffix = "th";
        }

        // Return the date with the suffix
        return dateStr.replaceFirst(day, day + suffix);
    }

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

    public static String formatTimeMillis(long millis) {
        Date date = new Date(millis);

        // Define the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("d 'of' MMMM HH:mm yyyy", Locale.ENGLISH);

        // Format the date
        String formattedDate = formatter.format(date);

        // Replace "of" with the appropriate suffix for the day
        formattedDate = addDaySuffix(formattedDate);

        return formattedDate;
    }
}
