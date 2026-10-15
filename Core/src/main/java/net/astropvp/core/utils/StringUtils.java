package net.astropvp.core.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class StringUtils {
    public static String formatWithCommas(int format) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(format);
        return numberAsString;
    }
}

