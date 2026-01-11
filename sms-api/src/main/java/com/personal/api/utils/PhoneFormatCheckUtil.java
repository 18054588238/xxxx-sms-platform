package com.personal.api.utils;

/**
 * @ClassName PhoneFormatCheckUtil
 * @Author liupanpan
 * @Date 2026/1/11
 * @Description
 */
public class PhoneFormatCheckUtil {
    private final static String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";

    public static boolean checkPhoneFormat(String phone) {
        return phone.matches(PHONE_REGEX);
    }
}
