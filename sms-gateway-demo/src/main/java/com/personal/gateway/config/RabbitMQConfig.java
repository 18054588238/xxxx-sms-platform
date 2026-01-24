package com.personal.gateway.config;

import static com.personal.common.constants.RabbitMQConstants.*;
import com.personal.common.constants.SmsConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitMQConfig
 * @Author liupanpan
 * @Date 2026/1/22
 * @Description
 * 局部配置：设置并行的线程数以及每个消息一次性拉取多少条消息
 * 在消费是指定containerFactory
 * 例如：@RabbitListener(queues = "${gateway.sendtopic}",containerFactory = "gatewayContainerFactory")
 */
@Configuration
public class RabbitMQConfig {

    /* 声明队列与交换机 -- 使用死信队列实现消息延迟发送
    * 实现流程：
    * 生产者（短信网关模块）发送消息到
    * 工作交换机 --- 根据绑定关系路由到（fanout广播到绑定队列） ---> 工作队列
    * --- TTL时间到 消息变为死信---> 进入死信交换机 --- 根据绑定关系路由到（fanout广播到绑定队列） ---> 死信队列
    * --- 消费者（搜索模块）监听死信队列中的消息
    * */

    /*死信交换机*/
    @Bean
    public Exchange deadExchange() {
        return ExchangeBuilder.fanoutExchange(SMS_GATEWAY_DEAD_EXCHANGE).build();
    }
    /*死信队列*/
    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(SMS_GATEWAY_DEAD_QUEUE)
                .build();
    }

    /*工作交换机*/
    @Bean
    public Exchange workExchange() {
        return ExchangeBuilder.fanoutExchange(SMS_GATEWAY_NORMAL_EXCHANGE).build();
    }

    /*工作队列*/
    @Bean
    public Queue workQueue() {
        return QueueBuilder.durable(SMS_GATEWAY_NORMAL_QUEUE)
                .withArgument("x-message-ttl",10000) // TTL 延迟10s
                .withArgument("x-dead-letter-exchange", SMS_GATEWAY_DEAD_EXCHANGE) // ttl到期后会路由到这里设置的死信交换机
                .withArgument("x-dead-letter-routing-key","")
                .build();
    }

    /*死信交换机和死信队列绑定*/
    // Spring会根据参数名称（参数名与方法名相同）自动注入对应的Bean。也可以使用@Qualifier显式指定
    @Bean
    public Binding deadBinding(Exchange deadExchange,Queue deadQueue) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with("").noargs();
    }

    /*工作队列和工作交换机绑定*/
    @Bean
    public Binding workBinding(Exchange workExchange,Queue workQueue) {
        return BindingBuilder.bind(workQueue).to(workExchange).with("").noargs();
    }

//    @Bean
    public SimpleRabbitListenerContainerFactory gatewayContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                                        ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();

        listenerContainerFactory.setConcurrentConsumers(5); // 消费者中并行的线程数
        listenerContainerFactory.setPrefetchCount(10); // 每个线程一次性最多拉取的消息个数，默认是250，可减少网络IO次数
        listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        configurer.configure(listenerContainerFactory,connectionFactory);

        return listenerContainerFactory;
    }
}
