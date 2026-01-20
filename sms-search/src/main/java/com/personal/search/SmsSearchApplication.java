package com.personal.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.personal.search","com.personal.common"})
public class SmsSearchApplication {
    public static void main( String[] args ) {
        SpringApplication.run(SmsSearchApplication.class, args);
    }
}
