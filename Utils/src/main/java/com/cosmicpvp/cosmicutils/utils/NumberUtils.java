/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.utils;

import com.cosmicpvp.cosmicutils.struct.DecimalFormatType;

public class NumberUtils {
    public static String format(double number, DecimalFormatType type) {
        return type.getFormat().format(number);
    }

    public static String formatMoney(double number) {
        return NumberUtils.format(number, DecimalFormatType.MONEY);
    }

    public static String formatSeconds(double number) {
        return NumberUtils.format(number, DecimalFormatType.SECONDS);
    }

    public static Long parseLongOrNull(String val) {
        try {
            return Long.parseLong(val);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Integer parseIntOrNull(String val) {
        try {
            return Integer.parseInt(val);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Double parseDoubleOrNull(String val) {
        try {
            return Double.parseDouble(val);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Float parseFloatOrNull(String val) {
        try {
            return Float.valueOf(Float.parseFloat(val));
        } catch (Exception exception) {
            return null;
        }
    }

    public static Number parseNumberOrNull(String val) {
        Integer found = NumberUtils.parseIntOrNull(val);
        if (found != null) {
            return found;
        }
        Long longFound = NumberUtils.parseLongOrNull(val);
        if (longFound != null) {
            return longFound;
        }
        Double doubleFound = NumberUtils.parseDoubleOrNull(val);
        if (doubleFound != null) {
            return doubleFound;
        }
        Float floatFound = NumberUtils.parseFloatOrNull(val);
        if (floatFound != null) {
            return floatFound;
        }
        return null;
    }
}

