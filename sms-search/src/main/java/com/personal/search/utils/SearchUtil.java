package com.personal.search.utils;

import java.time.LocalDate;

/**
 * @ClassName SearchUtil
 * @Author liupanpan
 * @Date 2026/1/29
 * @Description
 */
public class SearchUtil {

    private final static String indexNamePrefix = "sms_submit_log_";


    public static String getIndex() {
        return indexNamePrefix+getCurYear();
    }

    public static int getCurYear() {
        return LocalDate.now().getYear();
    }
}
