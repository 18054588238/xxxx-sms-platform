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
}
