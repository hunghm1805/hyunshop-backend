package com.hyunshop.common.util;

import static org.apache.logging.log4j.util.Strings.isEmpty;

public class StringUtils {

    public static boolean isNullOrBlank(String str) {
        if (str != null) str = str.trim();
        return str == null || str.isBlank();
    }

    public static boolean isNullOrBlank(Object str) {
        if (str != null) str = str.toString().trim();
        return str == null || ((String) str).isBlank();
    }
    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for(int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }
}

