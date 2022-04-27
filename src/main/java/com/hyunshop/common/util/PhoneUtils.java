package com.hyunshop.common.util;

public class PhoneUtils {

    public static String getPhone(String input) {
        input = input.trim();
        if (input.startsWith("+84")) {
            input = input.replace("+84", "0");
        }
        if (input.startsWith("84")) {
            input = "0" + input.substring(2);
        }
        return input;
    }

}
