package com.personal.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.personal.push","com.personal.common"})
public class SmsPushApplication {
    public static void main( String[] args ) {
        SpringApplication.run(SmsPushApplication.class, args);
    }
}
