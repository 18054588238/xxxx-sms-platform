package com.personal.search.listener;

import com.alibaba.fastjson.JSON;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;
import com.personal.search.service.ElasticsearchService;
import com.personal.search.utils.ThreadPoolUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName SmsSearchListener
 * @Author liupanpan
 * @Date 2026/1/20
 * @Description
 */
@Slf4j
@Component
public class SmsSearchListener {

    @Autowired
    ElasticsearchService elasticsearchService;

    private final String indexNamePrefix = "sms_submit_log_";

    @RabbitListener(queues = RabbitMQConstants.SMS_WRITE_LOG)
    public void consumer(StandardSubmit submit, Channel channel, Message message) throws IOException {
        log.info("【搜索模块】接收到日志消息：{}",submit);

        elasticsearchService.index(indexNamePrefix+getCurYear(), String.valueOf(submit.getSequenceId()), JSON.toJSONString(submit));
        // 手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    @RabbitListener(queues = RabbitMQConstants.SMS_GATEWAY_DEAD_QUEUE)
    public void updateConsumer(StandardReport report, Channel channel, Message message) throws IOException {
        log.info("【搜索模块】接收到待更新消息：{}",report);

        // 将report存到当前线程中，方便service获取
        ThreadPoolUtil.setReport(report);

        Map<String, Object> map = new HashMap<>();
        map.put("reportState", report.getReportState());

        elasticsearchService.update(indexNamePrefix+getCurYear(), String.valueOf(report.getSequenceId()), map);
        // 手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    private int getCurYear() {
        return LocalDate.now().getYear();
    }
}
