package com.personal.cache.controller;

import com.msb.framework.redis.RedisClient;
import com.personal.cache.model.TestUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @ClassName RedisTest
 * @Author liupanpan
 * @Date 2026/1/9
 * @Description
 */
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisClient redisClient;

    @Test
    public void testRedis() {
        redisClient.set("msb","test");

        TestUser user = new TestUser("yyqx", "1128");
        redisClient.set("msbObj",user);

        Object s = redisClient.get("msb");
        Object user1 = redisClient.get("msbObj");
        System.out.println(s);
        System.out.println(user1.toString());
    }
}
