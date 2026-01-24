package com.personal.gateway;

import cn.hippo4j.core.enable.EnableDynamicThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableDynamicThreadPool
@ComponentScan(basePackages = {"com.personal.gateway","com.personal.common"})
public class SmsGatewayApplication {
    public static void main( String[] args ) {
        SpringApplication.run(SmsGatewayApplication.class, args);
    }
}
