/*
 * Decompiled with CFR 0.150.
 */
package me.ifamasssxd.cosmiccontests.utils;

public class Utils {
    public static boolean isInt(String key) {
        try {
            Integer.parseInt(key);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean isDouble(String key) {
        try {
            Double.parseDouble(key);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean isLong(String key) {
        try {
            Long.parseLong(key);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

