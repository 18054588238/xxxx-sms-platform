package com.personal.common.constants;

/**
 * @ClassName RabbitMQConstants
 * @Author liupanpan
 * @Date 2026/1/11
 * @Description 声明RabbitMQ的队列信息
 */
public interface RabbitMQConstants {
    /**
     * 接口模块发送消息到策略模块的队列名称
     */
    String SMS_PRE_SEND = "sms_pre_send_topic";
    String MOBILE_AREA_OPERATOR = "mobile_area_operator_topic";
    String SMS_WRITE_LOG = "sms_write_log_topic";
    String SMS_PUSH_REPORT = "sms_push_report_topic";
    // 发送通道消息到网关模块的队列
    String SMS_GATEWAY = "sms_gateway_topic_";
}
