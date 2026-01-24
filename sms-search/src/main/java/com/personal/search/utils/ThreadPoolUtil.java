package com.personal.search.utils;

import com.personal.common.model.StandardReport;

/**
 * @ClassName ThreadPoolUtil
 * @Author liupanpan
 * @Date 2026/1/24
 * @Description
 */
public class ThreadPoolUtil {
    private static ThreadLocal<StandardReport> threadLocal = new ThreadLocal<>();


    public static void setReport(StandardReport standardReport) {
        threadLocal.set(standardReport);
    }

    public static StandardReport getReport() {
        return threadLocal.get();
    }

    // 防止内存泄漏
    public static void removeReport() {
        threadLocal.remove();
    }
}
