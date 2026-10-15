package com.astropvp.wildpvp.util;

public class TimeUtil {
    public static String getRemaining(int seconds, TimeUtil.TimeFormat type) {
        if (seconds < 60) {
            switch(type) {
                case DAYS:
                    return "0";
                case HOURS:
                    return "0";
                case MINUTES:
                    return "0";
                case SECONDS:
                    return String.valueOf(seconds);
                default:
                    return String.valueOf(seconds);
            }
        } else {
            int minutes = seconds / 60;
            int s = 60 * minutes;
            int secondsLeft = seconds - s;
            if (minutes < 60) {
                switch(type) {
                    case DAYS:
                        return "0";
                    case HOURS:
                        return "0";
                    case MINUTES:
                        return String.valueOf(minutes);
                    case SECONDS:
                        return String.valueOf(secondsLeft);
                    default:
                        return String.valueOf(seconds);
                }
            } else {
                int days;
                int inMins;
                int leftOver;
                if (minutes < 1440) {
                    days = minutes / 60;
                    inMins = 60 * days;
                    leftOver = minutes - inMins;
                    switch(type) {
                        case DAYS:
                            return "0";
                        case HOURS:
                            return String.valueOf(days);
                        case MINUTES:
                            return String.valueOf(leftOver);
                        case SECONDS:
                            return String.valueOf(secondsLeft);
                        default:
                            return String.valueOf(seconds);
                    }
                } else {
                    days = minutes / 1440;
                    inMins = 1440 * days;
                    leftOver = minutes - inMins;
                    if (leftOver < 60) {
                        switch(type) {
                            case DAYS:
                                return String.valueOf(days);
                            case HOURS:
                                return String.valueOf(0);
                            case MINUTES:
                                return String.valueOf(leftOver);
                            case SECONDS:
                                return String.valueOf(secondsLeft);
                            default:
                                return String.valueOf(seconds);
                        }
                    } else {
                        int hours = leftOver / 60;
                        int hoursInMins = 60 * hours;
                        int minsLeft = leftOver - hoursInMins;
                        switch(type) {
                            case DAYS:
                                return String.valueOf(days);
                            case HOURS:
                                return String.valueOf(hours);
                            case MINUTES:
                                return String.valueOf(minsLeft);
                            case SECONDS:
                                return String.valueOf(secondsLeft);
                            default:
                                return String.valueOf(seconds);
                        }
                    }
                }
            }
        }
    }

    public static String getTime(int seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else {
            int minutes = seconds / 60;
            int s = 60 * minutes;
            int secondsLeft = seconds - s;
            if (minutes < 60) {
                return secondsLeft > 0 ? String.valueOf(minutes + "m " + secondsLeft + "s") : String.valueOf(minutes + "m");
            } else {
                String time;
                int days;
                int inMins;
                int leftOver;
                if (minutes < 1440) {
                    days = minutes / 60;
                    time = days + "h";
                    inMins = 60 * days;
                    leftOver = minutes - inMins;
                    if (leftOver >= 1) {
                        time = time + " " + leftOver + "m";
                    }

                    if (secondsLeft > 0) {
                        time = time + " " + secondsLeft + "s";
                    }

                    return time;
                } else {
                    days = minutes / 1440;
                    time = days + "d";
                    inMins = 1440 * days;
                    leftOver = minutes - inMins;
                    if (leftOver >= 1) {
                        if (leftOver < 60) {
                            time = time + " " + leftOver + "m";
                        } else {
                            int hours = leftOver / 60;
                            time = time + " " + hours + "h";
                            int hoursInMins = 60 * hours;
                            int minsLeft = leftOver - hoursInMins;
                            time = time + " " + minsLeft + "m";
                        }
                    }

                    if (secondsLeft > 0) {
                        time = time + " " + secondsLeft + "s";
                    }

                    return time;
                }
            }
        }
    }

    public static enum TimeFormat {
        DAYS,
        HOURS,
        MINUTES,
        SECONDS;
    }
}
 