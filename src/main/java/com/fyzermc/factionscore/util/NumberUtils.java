package com.fyzermc.factionscore.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {

    private static final NumberFormat format = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

    @Deprecated
    public static Integer asInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Deprecated
    public static Double asDouble(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static String format(int number) {
        return format.format(number);
    }

    public static String format(double number) {
        return format.format(roundDouble(number, 2));
    }

    public static double roundDouble(double base, int decimalCases) {
        return new BigDecimal(base).setScale(decimalCases, RoundingMode.HALF_UP).doubleValue();
    }
}