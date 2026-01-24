package com.personal.gateway.netty4.utils;

import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName CmppSubmitMapUtil
 * @Author liupanpan
 * @Date 2026/1/22
 * @Description 用于CMPP发送短信时，临时存储 StandardReport对象，
 * 以便在运营商响应时可用获取到，在接收到响应后向es中写数据时要用到 StandardReport对象
 */
public class CmppReportMapUtil {
    private static ConcurrentHashMap<String, StandardReport> map = new ConcurrentHashMap<>();

    public static void put(String sequence,StandardReport standardSubmit) {
        map.put(sequence, standardSubmit);
    }
    public static StandardReport get(String sequence) {
        return map.remove(sequence);
    }
    public static StandardReport remove(String sequence) {
        return map.remove(sequence);
    }
}
