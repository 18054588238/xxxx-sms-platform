package com.personal.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.personal.monitor","com.personal.common"})
public class SmsMonitorApplication {
    public static void main( String[] args ) {
        SpringApplication.run(SmsMonitorApplication.class, args);
    }
}
