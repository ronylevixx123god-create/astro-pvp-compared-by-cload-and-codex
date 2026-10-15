package me.kingsavaqe.cagepvp.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class NumberUtil {
    private static final DecimalFormat decimalFormat = new DecimalFormat("##.#");

    public static String formatNumberByCommas(int number){
        return String.format("%,d", number);
    }

    public static String formatNumberByCommas(String number){
        String numberValue = "", numberDecimalValue = "";
        if(number.contains(".")) {
            numberDecimalValue = "." + number.split("\\.")[1];
            numberValue = number.replace(numberDecimalValue, "");
        } else{
            return String.format("%,d", new BigInteger(number));
        }
        if(numberDecimalValue.equals(".0")) numberDecimalValue = "";
        return String.format("%,d", new BigInteger(numberValue)) + numberDecimalValue;
    }

    public static String formatNumberByDecimal(float number){
        return decimalFormat.format(number);
    }

    public static String formatNumberBySuffix(double number){
        if(number < 1000.0D) return String.valueOf(number);
        int exp = (int) (Math.log(number) / Math.log(1000.0D));
        return String.format("%.1f%c", number / Math.pow(1000.0D, exp), "kMBTPE".charAt(exp - 1));
    }

    public static int formatNumberByText(String text) {
        return Integer.parseInt(text.replace("k", "000").replace("m", "000000").replace("b", "000000000"));
    }
}