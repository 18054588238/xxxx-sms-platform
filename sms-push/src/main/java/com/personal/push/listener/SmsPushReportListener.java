package com.personal.push.listener;

import com.alibaba.fastjson.JSON;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;
import com.personal.push.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @ClassName SmsPushReportListener
 * @Author liupanpan
 * @Date 2026/1/20
 * @Description
 */
@Component
@Slf4j
public class SmsPushReportListener {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final String SUCCESS = "SUCCESS";

    // 重试时间间隔
    private int[] delayTimes = {0,15000,30000,60000,300000};

    @RabbitListener(queues = RabbitMQConstants.SMS_PUSH_REPORT)
    public void consumer(StandardReport report, Channel channel, Message message) throws IOException {
        // 获取回调地址
        String callbackUrl = report.getCallbackUrl();
        if (StringUtils.isBlank(callbackUrl)) {
            log.info("【推送模块】消息推送失败，回调地址为空。callbackUrl={}",callbackUrl);
            // 手动ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // 给客户推送消息
        boolean flag = pushReport(report);

        retryPush(report,flag);


        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    private void retryPush(StandardReport report,boolean flag) {
        int reSendCount = report.getReSendCount();
        if (!flag) {
            // 推送失败，重试
            if (reSendCount < delayTimes.length-1) {
                log.info("【推送模块】开始第{}次重试消息推送。report={}",reSendCount+1,report);
                report.setReSendCount(reSendCount + 1);
                // 向延迟交换机发送消息
                rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_EXCHANGE, "", report, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        // 设置延迟时间
                        message.getMessageProperties().setDelay(delayTimes[report.getReSendCount()]);
                        return message;// 消息后处理器，用于在发送前设置消息的延迟时间
                    }
                });
            }
        } else {
            log.info("【推送模块】第{}次消息推送成功。report={}",reSendCount+1,report);
        }
    }

    private boolean pushReport(StandardReport report) {
        // 消息是否发送成功的标志
        boolean flag = false;
        String jsonBody = JSON.toJSONString(report);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            log.info("【推送模块】开始消息推送。report={}",report);
            String res = restTemplate.postForObject("http://" + report.getCallbackUrl(), new HttpEntity<>(jsonBody, httpHeaders), String.class);
            flag = SUCCESS.equals(res);
        } catch (RestClientException e) {
        }
        return flag;
    }

    // 监听延迟队列的消息
    @RabbitListener(queues = RabbitMQConfig.DELAYED_QUEUE)
    public void delayConsumer(StandardReport report, Channel channel, Message message) throws IOException {
        // 给客户推送消息
        boolean flag = pushReport(report);

        retryPush(report,flag);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
