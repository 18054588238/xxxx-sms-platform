package com.personal.push.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @ClassName RabbitMQConfig
 * @Author liupanpan
 * @Date 2026/1/21
 * @Description Spring AMQP 方式 - 声明式配置
 * Spring AMQP 内部工作原理
 * Spring 会做这些事情：
 *   1. 在应用启动时，收集所有 Exchange、Queue、Binding 类型的 Bean
 *   2. 通过 AmqpAdmin 自动声明到 RabbitMQ
 *   3. 处理声明失败的情况（重试、回滚等）
 *   4. 在应用关闭时，清理资源（可配置）
 */
@Configuration
public class RabbitMQConfig {

    public static final String DELAYED_EXCHANGE = "push_delayed_exchange";
    public static final String DELAYED_QUEUE = "push_delayed_queue";
    private static final String DELAYED_EXCHANGE_TYPE = "x-delayed-message"; // 表示该交换机是延迟交换机
    private static final String DELAYED_ROUTING_TYPE_KEY = "x-delayed-type"; // 指定延迟交换机的底层类型
    private static final String DELAYED_ROUTING_TYPE_FANOUT = "fanout";

    @Bean
    public Exchange delayedExchange() {
        HashMap<String, Object> args = new HashMap<>();
        args.put(DELAYED_ROUTING_TYPE_KEY,DELAYED_ROUTING_TYPE_FANOUT);
        return new CustomExchange(DELAYED_EXCHANGE,DELAYED_EXCHANGE_TYPE,false,false,args);
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE).build();
    }

    @Bean
    public Binding delayedBinding(Exchange delayedExchange,Queue delayedQueue) {
        return BindingBuilder
                .bind(delayedQueue)
                .to(delayedExchange)
                .with("") // 这里路由键为空字符串，可能适用于fanout交换机，因为fanout交换机会忽略路由键，将消息广播到所有绑定的队列。
                .noargs(); // 表示绑定没有额外的参数。
    }

}
