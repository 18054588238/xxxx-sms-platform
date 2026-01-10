package com.personal.cache.controller;

import com.msb.framework.redis.RedisClient;
import com.personal.cache.model.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName TestController
 * @Author liupanpan
 * @Date 2026/1/9
 * @Description
 */
@RestController
@Slf4j
public class TestController {
    @Autowired
    private RedisClient redisClient;

    @GetMapping("/redis")
    public void testRedis() {
        redisClient.set("msb","test");

        TestUser user = new TestUser("yyqx", "1128");
        redisClient.set("msbObj",user);

        Object s = redisClient.get("msb");
        Object user1 = redisClient.get("msbObj");
        System.out.println(s);
        System.out.println(user1);
    }


    @PostMapping("/setValue")
    public void setValue(@RequestParam String key, @RequestParam Object value) {
        redisClient.set(key,value);
        log.info("setValue:{}",value);
    }

    @PostMapping("/setMap")
    public void setMap(@RequestParam String key,@RequestBody Map<String,Object> ... value) {
        redisClient.sAdd(key,value);
        log.info("setMap:{}",value);
    }
}
