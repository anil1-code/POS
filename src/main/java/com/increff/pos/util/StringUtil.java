package com.increff.pos.util;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String trimAndLowerCase(String str) {
        return str.toLowerCase().trim();
    }

}
// TODO: 25/08/22 build a working user interface for both brond and product
// TODO: 25/08/22 then make a working backend for all the remaining ones
// TODO: 25/08/22 make changes according to feedback