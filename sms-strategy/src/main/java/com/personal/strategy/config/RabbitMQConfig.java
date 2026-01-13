package com.personal.strategy.config;

import com.personal.common.constants.RabbitMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitMQConfig
 * @Author liupanpan
 * @Date 2026/1/11
 * @Description 配置队列和交换机信息
 */
@Configuration
@Slf4j
public class RabbitMQConfig {
    /**
     * 接口模块发送消息到策略模块的队列
     * 只是告诉 RabbitMQ 要创建这个队列，但消息发送时需要指定交换机和路由键
     */
    @Bean
    public Queue preSendQueue() {
        return QueueBuilder.durable(RabbitMQConstants.MOBILE_AREA_OPERATOR).build();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 配置confirm和return机制，用于确保消息可靠地发送到RabbitMQ
        /*
        * publisher-confirm-type: correlated用于确认消息是否到达 交换机，RabbitMQ会返回一个ack（确认），通过实现ConfirmCallback接口来处理这些确认
        * 而publisher-returns: true用于处理消息无法从交换机路由到 队列 的情况。生产者可以通过实现ReturnCallback接口来处理这些被退回（没有到达队列）的消息
        * */

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack) {
                    // 表示消息没有到达交换机
                    log.error("[接口模块-发送消息] 消息没有到达交换机,correlationData = {}，cause = {}",
                            correlationData, cause);
                }
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                // 表示消息没有到达队列
                log.error("[接口模块-发送消息] 消息没有到达队列,message = {},replyCode = {},replyText = {},exchange = {},routingKey = {}",
                        new String(message.getBody()),replyCode,replyText,exchange,routingKey);
            }
        });

        return rabbitTemplate;
    }
}
