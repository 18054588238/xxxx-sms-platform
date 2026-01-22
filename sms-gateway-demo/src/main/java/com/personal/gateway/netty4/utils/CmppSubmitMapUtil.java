package com.personal.gateway.netty4.utils;

import com.personal.common.model.StandardSubmit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName CmppSubmitMapUtil
 * @Author liupanpan
 * @Date 2026/1/22
 * @Description 用于CMPP发送短信时，临时存储StandardSubmit对象，
 * 以便在运营商响应时可用获取到，在接收到响应后向es中写数据时要用到StandardSubmit对象
 */
public class CmppSubmitMapUtil {
    private static ConcurrentHashMap<Integer, StandardSubmit> map = new ConcurrentHashMap<>();

    public static void put(int sequence,StandardSubmit standardSubmit) {
        map.put(sequence, standardSubmit);
    }
    public static StandardSubmit get(int sequence) {
        return map.remove(sequence);
    }
    public static StandardSubmit remove(int sequence) {
        return map.remove(sequence);
    }
}
