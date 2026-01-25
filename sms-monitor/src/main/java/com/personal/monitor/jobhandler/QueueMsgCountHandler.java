package com.personal.monitor.jobhandler;

//import com.rabbitmq.client.ConnectionFactory;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.constants.SmsConstant;
import com.personal.monitor.feign.CacheFeignClient;
import com.personal.monitor.utils.MailUtil;
import com.personal.monitor.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName queueMsgCountHandler
 * @Author liupanpan
 * @Date 2026/1/25
 * @Description 监控队列的消息数量：接口模块 --> 策略模块 与 策略模块 --> 网关模块(需要查缓存 拼接得到客户名称)
 * 监控队列的收件人邮箱使用配置文件中写的
 */
@Component
@Slf4j
public class QueueMsgCountHandler {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Autowired
    private MailUtil mailUtil;

    String text = "<h1>您的队列消息队列堆积了，队名为%s，消息个数为%s。</h1>";

    // 两个队列一起监控
    @XxlJob("queueMsgCountHandler")
    public void monitor() {
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);

        // 接口模块 --> 策略模块 队列
        log.info("开始执行接口模块 --> 策略模块队列监控任务");
        monitorPreSendQueue(channel);
        log.info("执行接口模块 --> 策略模块队列监控任务完成");

        // 策略模块 --> 网关模块 队列
        log.info("开始执行策略模块 --> 网关模块队列监控任务");
        monitorGatewayQueue(channel);
        log.info("执行策略模块 --> 网关模块队列监控任务完成");

    }

    private void monitorGatewayQueue(Channel channel) {

        // 获取所有客户id
        Set<String> clientIds = cacheFeignClient.getScanKeys().stream()
                .map(key -> StringUtils.substringAfter(key,":")) // client_channel:63
                .collect(Collectors.toSet());

        for (String clientId : clientIds) {
            // 获取队列名
            String queueName = RabbitMQConstants.SMS_GATEWAY + clientId;

            // 获取队列消息数
            int msgCount = getQueueMagCount(channel,queueName);

            // 判断是否达到限定值
            if (msgCount > SmsConstant.MAX_MESSAGE_SIZE) {
                // 达到了就发送邮件给开发或运维人员
                mailUtil.sendSimpleMsg(String.format(text,queueName,msgCount),null,queueName + "队列消息堆积");
                log.info("队列：{},消息数为：{},达到限定值。",queueName,msgCount);
                return;
            }
            log.info("队列：{},消息数为：{},没有达到限定值。",queueName,msgCount);
        }
    }

    private void monitorPreSendQueue(Channel channel) {

        // 获取队列消息数
        int msgCount = getQueueMagCount(channel,RabbitMQConstants.SMS_PRE_SEND);

        // 判断是否达到限定值
        if (msgCount > SmsConstant.MAX_MESSAGE_SIZE) {
            // 达到了就发送邮件给开发或运维人员
            mailUtil.sendSimpleMsg(String.format(text,RabbitMQConstants.SMS_PRE_SEND,msgCount),null,RabbitMQConstants.SMS_PRE_SEND + "队列消息堆积");
            log.info("队列：{},消息数为：{},达到限定值。",RabbitMQConstants.SMS_PRE_SEND,msgCount);
            return;
        }
        log.info("队列：{},消息数为：{},没有达到限定值。",RabbitMQConstants.SMS_PRE_SEND,msgCount);
    }

    private int getQueueMagCount(Channel channel,String queueName) {
        int messageCount = 0;
        // 如果队列不存在，不会进行创建，直接记录日志信息
        messageCount = RabbitMQUtil.safeGetMessageCount(channel, queueName);
        return messageCount;
    }

}
