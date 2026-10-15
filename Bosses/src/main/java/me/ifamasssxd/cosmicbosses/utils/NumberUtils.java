package me.ifamasssxd.cosmicbosses.utils;

import java.text.DecimalFormat;

public class NumberUtils {
    static DecimalFormat format;

    public static String addCommas(final int number) {
        return NumberUtils.format.format(number);
    }

    static {
        NumberUtils.format = new DecimalFormat("#,##0");
    }
}
