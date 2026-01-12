package com.personal.strategy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SmsStrategyApplication {
    public static void main( String[] args ) {
        SpringApplication.run(SmsStrategyApplication.class, args);
    }
}
