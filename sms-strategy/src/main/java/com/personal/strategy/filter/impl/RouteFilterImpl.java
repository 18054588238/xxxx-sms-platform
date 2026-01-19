package com.personal.strategy.filter.impl;

import com.personal.common.constants.CacheConstant;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.StrategyException;
import com.personal.common.model.StandardSubmit;
import com.personal.strategy.feign.CacheFeignClient;
import com.personal.strategy.filter.ChainFilter;
import com.personal.strategy.utils.ErrorSendMsgUtil;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personal.strategy.utils.ChannelTransferUtil;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @ClassName ChainFilterImpl
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 路由校验
 */
@Slf4j
@Service(value = "route")
public class RouteFilterImpl implements ChainFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;
    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;
    // 用于管理AMQP资源（如队列、交换器、绑定等）
    @Autowired
    private AmqpAdmin amqpAdmin;
    // 用于发送和接收消息。
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /* AmqpAdmin 和 RabbitTemplate 的关系
    * 功能互补：AmqpAdmin用于管理AMQP资源，而RabbitTemplate用于消息的发送和接收。
    *         通常，在应用程序启动时，我们会使用AmqpAdmin来声明所需的队列、交换器和绑定，然后使用RabbitTemplate来进行消息操作。
    * 依赖关系：在Spring配置中，我们通常会同时配置这两个Bean。
    *         RabbitTemplate需要知道连接工厂，而AmqpAdmin（RabbitAdmin）也是基于连接工厂的。
    *         但是，它们之间没有直接的依赖关系，而是通过连接工厂与RabbitMQ服务器交互。
    * 使用场景：在应用程序启动阶段，我们可能使用AmqpAdmin来声明队列和交换器，确保它们存在。
    *         在业务逻辑中，我们使用RabbitTemplate来发送消息到这些交换器或从队列中接收消息。
    * */

    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-路由校验】   校验ing…………");
        Long clientId = submit.getClientId();
        // 获取客户关联的所有通道信息
        Set<Map> sMember = cacheFeignClient.getSMember(CacheConstant.CLIENT_CHANNEL + clientId);
        // Set<Map> 根据map中的clientChannelWeight降序排序
        TreeSet<Map> mapTreeSet = new TreeSet<>((o1, o2) -> {
            Integer o1weight = (Integer) o1.get("clientChannelWeight");
            Integer o2weight = (Integer) o2.get("clientChannelWeight");
            return o2weight.compareTo(o1weight);
        });
        mapTreeSet.addAll(sMember);

        boolean isSuccess = false;

        for (Map map : mapTreeSet) {
            // 判断是否可用
            if (!((Boolean) map.get("available"))) {
                // 该通道不可用，校验下一个通道
                continue;
            }

            // 查询通道的详细信息
            Map channelMap = cacheFeignClient.hGetAll(CacheConstant.CHANNEL + map.get("channelId"));
            // 判断通道关联的运营商和客户的运营商是否相等
            Integer channelType = (Integer) channelMap.get("channelType");
            if ( channelType != 0 && !channelType.equals(submit.getOperatorId())) {
                // 该通道不可用，校验下一个通道
                continue;
            }
            // 如果后期涉及到的通道的转换，这里留一个口子
            Map selectedChannel = ChannelTransferUtil.transfer(submit, channelMap);
            // 校验通过
            isSuccess = true;
            // 封装信息
            submit.setChannelId(Long.parseLong(channelMap.get("id")+""));
            submit.setSrcNumber(""+selectedChannel.get("channelNumber") + map.get("clientChannelNumber"));

            break;
        }
        if (!isSuccess) {
            log.info("【策略模块-路由校验】当前客户没有匹配的路由通道，client_id = {}",clientId);
            submit.setErrorMsg(ExceptionEnums.NO_ROUTE_CHANNEL.getMessage()+"client_id =" + clientId);
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.NO_ROUTE_CHANNEL);
        }

        try {
            // 构建动态队列并发送消息到队列中
            String queueName = RabbitMQConstants.SMS_GATEWAY + submit.getChannelId(); // 队列名称
            amqpAdmin.declareQueue(QueueBuilder.durable(queueName).build()); // 构建队列
            rabbitTemplate.convertAndSend(queueName,submit);
            log.info("【策略模块-路由校验】校验成功！");
        } catch (AmqpException e) {
            log.info("【策略模块-路由校验】声明通道队列队列以及发送消息出现问题，client_id = {}",clientId);
            submit.setErrorMsg(e.getMessage());
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(e.getMessage(),ExceptionEnums.UNKNOWN_ERROR.getCode());
        }

    }
}
