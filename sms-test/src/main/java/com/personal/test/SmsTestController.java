package com.personal.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName SmsTestController
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class SmsTestController {
    public static void main(String[] args) {
        SpringApplication.run(SmsTestController.class,args);
    }
}
