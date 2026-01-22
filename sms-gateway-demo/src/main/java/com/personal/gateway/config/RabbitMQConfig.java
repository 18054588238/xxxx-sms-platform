package com.personal.gateway.config;

import org.springframework.amqp.core.AcknowledgeMode;
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

    @Bean
    public SimpleRabbitListenerContainerFactory gatewayContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                                        ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();

        listenerContainerFactory.setConcurrentConsumers(10); // 消费者中并行的线程数
        listenerContainerFactory.setPrefetchCount(50); // 每个线程一次性最多拉取的消息个数，默认是250，可减少网络IO次数
        listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        configurer.configure(listenerContainerFactory,connectionFactory);

        return listenerContainerFactory;
    }
}
