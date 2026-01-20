package com.personal.search.listener;

import com.alibaba.fastjson.JSON;
import com.personal.common.constants.CacheConstant;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.model.StandardSubmit;
import com.personal.search.service.ElasticsearchService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @ClassName SmsWriteLogListener
 * @Author liupanpan
 * @Date 2026/1/20
 * @Description
 */
@Slf4j
@Component
public class SmsWriteLogListener {

    @Autowired
    ElasticsearchService elasticsearchService;

    private final String indexNamePrefix = "sms_submit_log_";

    @RabbitListener(queues = RabbitMQConstants.SMS_WRITE_LOG)
    public void consumer(StandardSubmit submit, Channel channel, Message message) throws IOException {
        log.info("【搜索模块】接收到错误日志消息：{}",submit);

        elasticsearchService.index(indexNamePrefix+getCurYear(), String.valueOf(submit.getSequenceId()), JSON.toJSONString(submit));
        // 手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    private int getCurYear() {
        return LocalDate.now().getYear();
    }
}
