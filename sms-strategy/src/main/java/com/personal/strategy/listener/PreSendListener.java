package com.personal.strategy.listener;

import com.personal.common.constants.CacheConstant;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.constants.SmsConstant;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.StrategyException;
import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;
import com.personal.common.utils.MapStructUtil;
import com.personal.strategy.feign.CacheFeignClient;
import com.personal.strategy.filter.ChainFilterContext;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @ClassName PreSendListener
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 监听rabbitmq中的消息
 * 作用：
 * 手动确认模式允许开发者更精确地控制消息的确认时机，确保消息被成功处理后再进行确认，避免消息丢失。
 * 如果消息处理过程中发生异常，可以选择不确认消息，这样消息会重新回到队列（取决于配置）或者进入死信队列，从而保证消息的可靠处理。
 * 如何使用：
 * 在配置文件中设置acknowledge-mode为manual。
 * 在消息监听方法中，通过Channel参数手动进行消息确认。
 * /*
 *         * basicAck 用于确认消息，参数为：deliveryTag（消息标签）和multiple（是否批量确认，一般设置为false）。
 *         * basicNack 用于否定确认，参数为：deliveryTag、multiple和requeue（是否重新入队）。
 *         * basicReject 用于拒绝消息，参数为：deliveryTag和requeue，但不能批量拒绝。
 */
@Component
@Slf4j
public class PreSendListener {

    @Autowired
    private ChainFilterContext chainFilterContext;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CacheFeignClient cacheFeignClient;
    @Autowired
    private MapStructUtil mapStructUtil;

    @RabbitListener(queues = RabbitMQConstants.SMS_PRE_SEND)
    public void preSend(StandardSubmit submit, Message message, Channel channel) throws IOException { // 获取发送的数据

        log.info("[策略模块] 接收消息 ,submit = {}", submit);

        try {
            // 进行校验
            chainFilterContext.checkManagement(submit);
            log.info("[策略模块] 接收消息 ,校验成功");
            // 处理成功后，手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (StrategyException e) {
            log.warn("[策略模块] 校验不通过，接收消息失败,msg: {}",e.getMessage());
        }
    }
}
