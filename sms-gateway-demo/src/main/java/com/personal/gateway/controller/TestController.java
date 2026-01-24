package com.personal.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName TestController
 * @Author liupanpan
 * @Date 2026/1/24
 * @Description
 */
@RestController
public class TestController {
    @Autowired
    private ThreadPoolExecutor cmppSubmitDynamicExecutor;

    @GetMapping("/test")
    String contextLoads() {
        cmppSubmitDynamicExecutor.execute(() ->{
            System.out.println(Thread.currentThread().getName()+"-线程池大小："+cmppSubmitDynamicExecutor.getPoolSize()+"ok!");
        });
        return "ok";
    }
}
