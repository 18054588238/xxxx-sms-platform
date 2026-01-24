package com.personal.gateway.config;

import cn.hippo4j.core.executor.DynamicThreadPool;
import cn.hippo4j.core.executor.support.ThreadPoolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName ThreadPoolConfig
 * @Author liupanpan
 * @Date 2026/1/24
 * @Description ThreadPoolExecutor 适配
 * 添加线程池配置类，通过 @DynamicThreadPool 注解修饰。threadPoolId 为服务端创建的线程池 ID。
 *
 * 通过 ThreadPoolBuilder 构建动态线程池，只有 threadFactory、threadPoolId 为必填项，其它参数会从 hippo4j-server 服务拉取。
 *
 * 引入线程池，处理运营商的应答，提高处理速度，防止消息堆积
 * 两类消息：
 *  1. 接收到短信提交应答 - 第一次响应
 *  2. 状态报告的应答 - 第二次响应
 */
@Configuration
public class ThreadPoolConfig {
    /* 声明两个线程池信息。*/
    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor cmppSubmitDynamicExecutor() {
        String threadPoolId = "cmpp-submit"; // 处理运营商的第一次响应
        ThreadPoolExecutor messageConsumeDynamicExecutor = ThreadPoolBuilder.builder()
                .threadFactory(threadPoolId) // 指定线程名称的前缀
                .threadPoolId(threadPoolId) // 线程池在Hippo4j中的唯一标识
                .dynamicPool() // 代表动态线程池
                .build();
        return messageConsumeDynamicExecutor;
    }

    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor cpmmDeliverDynamicExecutor() {
        String threadPoolId = "cmpp-deliver"; // 处理运营商的第二次响应
        ThreadPoolExecutor messageProduceDynamicExecutor = ThreadPoolBuilder.builder()
                .threadFactory(threadPoolId)
                .threadPoolId(threadPoolId)
                .dynamicPool()
                .build();
        return messageProduceDynamicExecutor;
    }
}
