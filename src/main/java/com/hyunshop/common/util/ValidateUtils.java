package com.hyunshop.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

public class ValidateUtils {
    //HashMap<name, values>
    public static void validateNullOrBlankString(HashMap<String, String> data) {
        for (String key : data.keySet()) {
            if (StringUtils.isNullOrBlank(data.get(key))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không được để trống " + key + "!");
            }
        }
    }

    public static void validateNullOrBlank(HashMap<String, Object> data) {
        for (String key : data.keySet()) {
            if (StringUtils.isNullOrBlank(data.get(key))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không được để trống " + key + "!");
            }
        }
    }

    public static void validateNullOrNegativeNumber(HashMap<String, Long> data) {
        for (String key : data.keySet()) {
            if (data.get(key) == null || data.get(key) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không được để trống " + key + "!");
            }
        }
    }

    public static void isEmail(String email) {
        ValidateUtils.validateNullOrBlankString(email, "email");
        String reg = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        if (!email.trim().matches(reg)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không hợp lệ!");
        }
    }

    private static void validateNullOrBlankString(String email, String name) {
        if (StringUtils.isNullOrBlank(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không được để trống " + name + "!");
        }
    }

    public static void isPhoneNumber(String phone) {
        ValidateUtils.validateNullOrBlankString(phone, "số điện thoại");
        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
        if (!phone.matches(reg)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại không hợp lệ!");
        }
    }

    public static void isPassword(String password) {
        if (password.trim().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu phải dài hơn 6 ký tự!");
        }
    }

    public static void validateNullOrBlankList(List<Object> list, String name) {
        if (list == null || list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh sách " + name + " không được trống!");
        }
    }

    public static void validateNullOrNegativeNumber(String price, String name) {
        ValidateUtils.validateNullOrBlankString(price, name);

        try {
            if (Long.parseLong(price.trim()) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + " phải lớn hơn 0!");
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + " sai định dạng!");

        }


    }
}
