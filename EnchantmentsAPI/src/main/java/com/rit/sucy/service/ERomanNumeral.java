/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package com.rit.sucy.service;

import java.util.HashMap;
import org.apache.commons.lang.Validate;

public class ERomanNumeral {
    private static HashMap<String, Integer> cachedRomanConverts = new HashMap();

    public static String numeralOf(int value) {
        Validate.isTrue((value > 0 ? 1 : 0) != 0, (String)"Roman numbers can't express zero or negative numbers!");
        StringBuilder builder = new StringBuilder();
        RomanNumber[] romanNumbers = RomanNumber.values();
        for (int i = 0; i < romanNumbers.length; ++i) {
            RomanNumber romanNumber = romanNumbers[i];
            while (value >= romanNumber.getInDecimal()) {
                value -= romanNumber.getInDecimal();
                builder.append(romanNumber.name());
            }
            if (i >= romanNumbers.length - 1) continue;
            int index = i - i % 2 + 2;
            RomanNumber subtractNum = romanNumbers[index];
            if (value < romanNumber.getInDecimal() - subtractNum.getInDecimal()) continue;
            value -= romanNumber.getInDecimal() - subtractNum.getInDecimal();
            builder.append(subtractNum.name());
            builder.append(romanNumber.name());
        }
        return builder.toString();
    }

    public static int getValueOf(String romanNumeral) {
        if (cachedRomanConverts.containsKey(romanNumeral)) {
            return cachedRomanConverts.get(romanNumeral);
        }
        char[] numerals = romanNumeral.toCharArray();
        int total = 0;
        for (int i = 0; i < numerals.length; ++i) {
            int value = ERomanNumeral.getNumeralValue(numerals[i]);
            if (i < numerals.length - 1 && ERomanNumeral.getNumeralValue(numerals[i + 1]) > value) {
                value = -value;
            }
            if (value == 0) {
                return 0;
            }
            total += value;
        }
        cachedRomanConverts.put(romanNumeral, total);
        return total;
    }

    public static int getNumeralValue(char numeral) {
        String romanNumeral = ("" + numeral).toUpperCase();
        try {
            RomanNumber romanNumber = RomanNumber.valueOf(romanNumeral);
            return romanNumber.getInDecimal();
        }
        catch (IllegalArgumentException e) {
            return 0;
        }
    }

    private static enum RomanNumber {
        M(1000),
        D(500),
        C(100),
        L(50),
        X(10),
        V(5),
        I(1);

        private final int valueInDec;

        private RomanNumber(int decimal) {
            this.valueInDec = decimal;
        }

        public int getInDecimal() {
            return this.valueInDec;
        }
    }
}

