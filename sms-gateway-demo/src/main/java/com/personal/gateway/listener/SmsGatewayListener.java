package com.personal.gateway.listener;

import com.personal.common.model.StandardSubmit;
import com.personal.gateway.netty4.NettyClient;
import com.personal.gateway.netty4.entity.CmppMessageHeader;
import com.personal.gateway.netty4.entity.CmppSubmit;
import com.personal.gateway.netty4.utils.CmppSubmitMapUtil;
import com.personal.gateway.netty4.utils.Command;
import com.personal.gateway.netty4.utils.MsgUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName SmsGatewayListener
 * @Author liupanpan
 * @Date 2026/1/22
 * @Description 监听策略模块发送的消息，并处理
 */
@Component
@Slf4j
public class SmsGatewayListener {

    @Autowired
    private NettyClient nettyClient;

//    @RabbitListener(queues = "${gateway.sendTopic}",containerFactory = "gatewayContainerFactory") // 局部配置。多线程并行消费rabbitmq消息
    @RabbitListener(queues = "${gateway.sendTopic}")
    public void consumer(StandardSubmit submit, Channel channel, Message message) throws IOException {
        log.info("[短信网管模块] 接收到消息 submit = {}",submit);
        // 和运营商交互，发送一次请求，接收两次响应
        String mobile = submit.getMobile();
        String srcNumber = submit.getSrcNumber();
        String text = submit.getText();

        int sequence = MsgUtils.getSequence();

        CmppSubmit cmppSubmit = new CmppSubmit(Command.CMPP2_VERSION,srcNumber,sequence,mobile,text);
        nettyClient.submit(cmppSubmit); // 和运营商通信

        // submit保存下来，以便在接收到响应后向es中写数据时使用
        CmppSubmitMapUtil.put(sequence,submit);

        // 手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
