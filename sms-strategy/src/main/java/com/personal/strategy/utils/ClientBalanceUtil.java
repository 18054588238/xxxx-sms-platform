package com.personal.strategy.utils;

/**
 * @ClassName ClientBalanceUtil
 * @Author liupanpan
 * @Date 2026/1/18
 * @Description
 */
public class ClientBalanceUtil {
    /**
     * todo 后续如果要给客户指定欠费的额度等级，再重写方法
     * 允许的欠费额度
     */
    public static Long getClientAmountLimit(Long clientId) {
        return -1000L;
    }
}
