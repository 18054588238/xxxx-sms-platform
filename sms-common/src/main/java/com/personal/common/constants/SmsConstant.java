package com.personal.common.constants;

/**
 * @ClassName SmsConstant
 * @Author liupanpan
 * @Date 2026/1/15
 * @Description
 */
public interface SmsConstant {
    // 运营商第一次响应结果为0表示正确
    int OPERATOR_STATE_SUCCESS = 0;
    // 短信发送成功
    int REPORT_STATE_SUCCESS = 1;
    // 短信发送失败
    int REPORT_STATE_FAIL = 2;
    int CODE_TYPE = 0;
    int NOTIFY_TYPE = 1;
    int MARKET_TYPE = 2;

    // 表示短信最终发送成功
    String DELIVRD = "DELIVRD";

    // 队列消息的最大预警数
    int MAX_MESSAGE_SIZE = 0;
}
