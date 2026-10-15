/*
 * Decompiled with CFR 0.150.
 */
package me.ifamasssxd.cosmicguishop.utils;

import java.text.DecimalFormat;

public class NumberUtils {
    private static DecimalFormat formatter = new DecimalFormat("#");

    public static String formatMoney(long balance) {
        if ((double)balance >= 1.0E12) {
            if ((double)balance % 1.0E12 == 0.0) {
                return String.valueOf(formatter.format((double)balance / 1.0E12)) + "Trillion";
            }
            return String.valueOf(balance);
        }
        if ((double)balance >= 1.0E9) {
            if ((double)balance % 1.0E9 == 0.0) {
                return String.valueOf(formatter.format((double)balance / 1.0E9)) + "Billion";
            }
            return String.valueOf(balance);
        }
        if ((double)balance >= 1000000.0) {
            if ((double)balance % 1000000.0 == 0.0) {
                return String.valueOf(formatter.format((double)balance / 1000000.0)) + "Million";
            }
            return String.valueOf(balance);
        }
        if (balance < 1000L) {
            if (balance % 1000L == 0L) {
                return String.valueOf(formatter.format((double)balance / 1000.0)) + "k";
            }
            return String.valueOf(balance);
        }
        return String.valueOf(balance);
    }
}

