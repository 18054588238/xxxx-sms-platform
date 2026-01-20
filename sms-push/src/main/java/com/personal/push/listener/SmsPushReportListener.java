package com.personal.push.listener;

import com.alibaba.fastjson.JSON;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    private final String SUCCESS = "SUCCESS";

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

        if (!flag) {
            // 推送失败，重试
            log.info("【推送模块】开始重试消息推送。report={}",report);

        }
        log.info("【推送模块】消息推送成功。report={}",report);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
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
            throw new RuntimeException(e);
        }
        return flag;
    }
}
