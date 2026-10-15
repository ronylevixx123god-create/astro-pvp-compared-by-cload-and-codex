package me.venir.astrocollectors.utils;

import org.bukkit.ChatColor;

import java.text.NumberFormat;
import java.util.List;

public class Utils {
    public static String format(String string) {
        char[] b = string.toCharArray();

        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }


    public static List<String> format(List<String> stringList) {
        stringList.replaceAll(Utils::format);
        return stringList;
    }

    private static final NumberFormat nf = NumberFormat.getIntegerInstance();

    public static String formatNum(int number) {
        return nf.format(number);
    }

    public static String formatNum(long number) {
        return nf.format(number);
    }

    public static String formatNum(double number) {
        return nf.format(number);
    }

    public static String formatNum(float number) {
        return nf.format(number);
    }

    public static String splitToComponentTimes(long seconds) {
        int hours = (int) seconds / 3600;
        int remainder = (int) seconds - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        return hours + "h " + mins + "m " + secs + "s ";
    }
}
