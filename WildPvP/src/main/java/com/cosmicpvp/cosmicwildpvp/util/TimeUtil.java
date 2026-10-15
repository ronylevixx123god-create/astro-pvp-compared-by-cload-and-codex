package com.cosmicpvp.cosmicwildpvp.util;

public class TimeUtil
{
    public static String getRemaining(final int seconds, final TimeFormat type) {
        if (seconds < 60) {
            switch (type) {
                case DAYS: {
                    return "0";
                }
                case HOURS: {
                    return "0";
                }
                case MINUTES: {
                    return "0";
                }
                case SECONDS: {
                    return String.valueOf(seconds);
                }
                default: {
                    return String.valueOf(seconds);
                }
            }
        }
        else {
            final int minutes = seconds / 60;
            final int s = 60 * minutes;
            final int secondsLeft = seconds - s;
            if (minutes < 60) {
                switch (type) {
                    case DAYS: {
                        return "0";
                    }
                    case HOURS: {
                        return "0";
                    }
                    case MINUTES: {
                        return String.valueOf(minutes);
                    }
                    case SECONDS: {
                        return String.valueOf(secondsLeft);
                    }
                    default: {
                        return String.valueOf(seconds);
                    }
                }
            }
            else if (minutes < 1440) {
                final int i = minutes / 60;
                final int j = 60 * i;
                final int k = minutes - j;
                switch (type) {
                    case DAYS: {
                        return "0";
                    }
                    case HOURS: {
                        return String.valueOf(i);
                    }
                    case MINUTES: {
                        return String.valueOf(k);
                    }
                    case SECONDS: {
                        return String.valueOf(secondsLeft);
                    }
                    default: {
                        return String.valueOf(seconds);
                    }
                }
            }
            else {
                final int days = minutes / 1440;
                final int inMins = 1440 * days;
                final int leftOver = minutes - inMins;
                if (leftOver < 60) {
                    switch (type) {
                        case DAYS: {
                            return String.valueOf(days);
                        }
                        case HOURS: {
                            return String.valueOf(0);
                        }
                        case MINUTES: {
                            return String.valueOf(leftOver);
                        }
                        case SECONDS: {
                            return String.valueOf(secondsLeft);
                        }
                        default: {
                            return String.valueOf(seconds);
                        }
                    }
                }
                else {
                    final int hours = leftOver / 60;
                    final int hoursInMins = 60 * hours;
                    final int minsLeft = leftOver - hoursInMins;
                    switch (type) {
                        case DAYS: {
                            return String.valueOf(days);
                        }
                        case HOURS: {
                            return String.valueOf(hours);
                        }
                        case MINUTES: {
                            return String.valueOf(minsLeft);
                        }
                        case SECONDS: {
                            return String.valueOf(secondsLeft);
                        }
                        default: {
                            return String.valueOf(seconds);
                        }
                    }
                }
            }
        }
    }

    public static String getTime(final int seconds) {
        if (seconds < 60) {
            return seconds + "s";
        }
        final int minutes = seconds / 60;
        final int s = 60 * minutes;
        final int secondsLeft = seconds - s;
        if (minutes < 60) {
            return (secondsLeft > 0) ? String.valueOf(minutes + "m " + secondsLeft + "s") : String.valueOf(minutes + "m");
        }
        if (minutes < 1440) {
            final int i = minutes / 60;
            String str = i + "h";
            final int j = 60 * i;
            final int k = minutes - j;
            if (k >= 1) {
                str = str + " " + k + "m";
            }
            if (secondsLeft > 0) {
                str = str + " " + secondsLeft + "s";
            }
            return str;
        }
        final int days = minutes / 1440;
        String time = days + "d";
        final int inMins = 1440 * days;
        final int leftOver = minutes - inMins;
        if (leftOver >= 1) {
            if (leftOver < 60) {
                time = time + " " + leftOver + "m";
            }
            else {
                final int hours = leftOver / 60;
                time = time + " " + hours + "h";
                final int hoursInMins = 60 * hours;
                final int minsLeft = leftOver - hoursInMins;
                time = time + " " + minsLeft + "m";
            }
        }
        if (secondsLeft > 0) {
            time = time + " " + secondsLeft + "s";
        }
        return time;
    }

    public enum TimeFormat
    {
        DAYS,
        HOURS,
        MINUTES,
        SECONDS;
    }
}
