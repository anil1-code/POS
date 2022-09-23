package com.increff.pos.util;

import java.text.DecimalFormat;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static Double truncateDouble(Double d) {
        return Double.valueOf(DECIMAL_FORMAT.format(d));
    }

    public static int length(String str) {
        return isEmpty(str) ? 0 : str.length();
    }

    public static String trimAndLowerCase(String str) {
        return str.toLowerCase().trim();
    }

}
// TODO: 25/08/22 build a working user interface for both brond and product
// TODO: 25/08/22 then make a working backend for all the remaining ones
// TODO: 25/08/22 make changes according to feedback