/*
 * Decompiled with CFR 0.150.
 */
package me.ifamasssxd.cosmicmysterychest.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("EEE MM/dd/yy");

    public static String getFormalTimeRemaining(long millisecondsTimestamp) {
        return TimeUtil.getFormalTimeRemaining(millisecondsTimestamp, false);
    }

    public static String formatDate(long time) {
        return format.format(new Date(time));
    }

    public static String formatDate(long time, String formatter) {
        return new SimpleDateFormat(formatter).format(new Date(time));
    }

    public static String formatDate(long time, SimpleDateFormat format) {
        return format.format(new Date(time));
    }

    public static String getFormalTimeRemaining(long millisecondsTimestamp, boolean useShortUnits) {
        long span = millisecondsTimestamp - System.currentTimeMillis();
        String ret = TimeUtil.getFormalTimeFromMillis(span, useShortUnits);
        return ret.isEmpty() ? "0 seconds" : (useShortUnits ? "0s" : ret);
    }

    public static String formatTimeSincePastTime(long pastTime, boolean useShort) {
        return TimeUtil.getFormalTimeFromMillis(System.currentTimeMillis() - pastTime, useShort);
    }

    public static String formatFutureTime(long timestamp) {
        return TimeUtil.getFormalTimeFromMillis(timestamp - System.currentTimeMillis());
    }

    public static String formatFutureTime(long timestamp, boolean useShortUnits, boolean includeSeconds) {
        return TimeUtil.getFormalTimeFromMillis(timestamp - System.currentTimeMillis(), useShortUnits, includeSeconds);
    }

    public static String formatSeconds(int seconds) {
        return TimeUtil.getFormalTimeFromMillis((long) seconds * 1000L);
    }

    public static String formatSeconds(int seconds, boolean shortUnits) {
        return TimeUtil.getFormalTimeFromMillis((long) seconds * 1000L, shortUnits, true);
    }

    public static String getFormalTimeFromMillis(long timeInMilliseconds) {
        return TimeUtil.getFormalTimeFromMillis(timeInMilliseconds, false);
    }

    public static String getFormalTimeFromMillis(long timeInMilliseconds, boolean useShortUnits) {
        return TimeUtil.getFormalTimeFromMillis(timeInMilliseconds, useShortUnits, true);
    }

    public static String getFormalTimeFromMillis(long timeInMilliseconds, boolean useShortUnits, boolean includeSeconds) {
        String[] arrstring;
        if (timeInMilliseconds < 60000L && !includeSeconds) {
            return useShortUnits ? "< 1m" : "< 1 minute";
        }
        if (timeInMilliseconds < 1000L) {
            return useShortUnits ? "0s" : "0 seconds";
        }
        StringBuilder time = new StringBuilder();
        double[] timesList = new double[4];
        if (useShortUnits) {
            String[] arrstring2 = new String[4];
            arrstring2[0] = "s";
            arrstring2[1] = "m";
            arrstring2[2] = "h";
            arrstring = arrstring2;
            arrstring2[3] = "d";
        } else {
            String[] arrstring3 = new String[4];
            arrstring3[0] = "second";
            arrstring3[1] = "minute";
            arrstring3[2] = "hour";
            arrstring = arrstring3;
            arrstring3[3] = "day";
        }
        String[] unitsList = arrstring;
        timesList[0] = timeInMilliseconds / 1000L;
        timesList[1] = Math.floor(timesList[0] / 60.0);
        double[] array = timesList;
        boolean n = false;
        double[] array4 = array;
        boolean n4 = false;
        array4[0] = array4[0] - timesList[1] * 60.0;
        timesList[2] = Math.floor(timesList[1] / 60.0);
        double[] array2 = timesList;
        boolean n2 = true;
        double[] array5 = array2;
        boolean n5 = true;
        array5[1] = array5[1] - timesList[2] * 60.0;
        timesList[3] = Math.floor(timesList[2] / 24.0);
        double[] array3 = timesList;
        int n3 = 2;
        double[] array6 = array3;
        int n6 = 2;
        array6[2] = array6[2] - timesList[3] * 24.0;
        for (int j = 3; j > -1; --j) {
            double d = timesList[j];
            if (!(d >= 1.0)) continue;
            if (j == 0 && !includeSeconds) break;
            if (useShortUnits) {
                time.append((int) d).append(unitsList[j]).append(" ");
                continue;
            }
            time.append((int) d).append(" ").append(unitsList[j]).append(d > 1.0 ? "s " : " ");
        }
        return time.toString().trim();
    }

    public static long parseDateDiff(String time, boolean future) throws Exception {
        Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if (m.group() == null || m.group().isEmpty()) continue;
            for (int i = 0; i < m.groupCount(); ++i) {
                if (m.group(i) == null || m.group(i).isEmpty()) continue;
                found = true;
                break;
            }
            if (!found) continue;
            if (m.group(1) != null && !m.group(1).isEmpty()) {
                years = Integer.parseInt(m.group(1));
            }
            if (m.group(2) != null && !m.group(2).isEmpty()) {
                months = Integer.parseInt(m.group(2));
            }
            if (m.group(3) != null && !m.group(3).isEmpty()) {
                weeks = Integer.parseInt(m.group(3));
            }
            if (m.group(4) != null && !m.group(4).isEmpty()) {
                days = Integer.parseInt(m.group(4));
            }
            if (m.group(5) != null && !m.group(5).isEmpty()) {
                hours = Integer.parseInt(m.group(5));
            }
            if (m.group(6) != null && !m.group(6).isEmpty()) {
                minutes = Integer.parseInt(m.group(6));
            }
            if (m.group(7) == null || m.group(7).isEmpty()) break;
            seconds = Integer.parseInt(m.group(7));
            break;
        }
        if (!found) {
            throw new Exception("Illegal Date");
        }
        if (years > 20) {
            throw new Exception("Illegal Date");
        }
        GregorianCalendar c = new GregorianCalendar();
        if (years > 0) {
            ((Calendar) c).add(1, years * (future ? 1 : -1));
        }
        if (months > 0) {
            ((Calendar) c).add(2, months * (future ? 1 : -1));
        }
        if (weeks > 0) {
            ((Calendar) c).add(3, weeks * (future ? 1 : -1));
        }
        if (days > 0) {
            ((Calendar) c).add(5, days * (future ? 1 : -1));
        }
        if (hours > 0) {
            ((Calendar) c).add(11, hours * (future ? 1 : -1));
        }
        if (minutes > 0) {
            ((Calendar) c).add(12, minutes * (future ? 1 : -1));
        }
        if (seconds > 0) {
            ((Calendar) c).add(13, seconds * (future ? 1 : -1));
        }
        return c.getTimeInMillis() / 1000L;
    }

    public static String formatTimeToDate(long time, String format, String timeZone) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String dayNumberSuffix = TimeUtil.getDayNumberSuffix(cal.get(5));
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

