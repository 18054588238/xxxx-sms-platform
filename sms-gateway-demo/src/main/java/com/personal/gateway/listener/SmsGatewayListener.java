package com.personal.gateway.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName SmsGatewayListener
 * @Author liupanpan
 * @Date 2026/1/22
 * @Description
 */
@Component

public class SmsGatewayListener {

//    @RabbitListener(queues = "${gateway.sendTopic}",containerFactory = "gatewayContainerFactory") // 局部配置。多线程并行消费rabbitmq消息
    @RabbitListener(queues = "${gateway.sendTopic}")
    public void consumer(StandardSubmit submit, Channel channel, Message message) throws IOException {

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
