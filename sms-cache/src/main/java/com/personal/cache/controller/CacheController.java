package com.personal.cache.controller;

import com.msb.framework.redis.RedisClient;
import com.personal.cache.model.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName TestController
 * @Author liupanpan
 * @Date 2026/1/9
 * @Description
 */
@RestController
@Slf4j
@RequestMapping("/cache")
public class CacheController {
    @Autowired
    private RedisClient redisClient;


    @GetMapping("/getMap")
    Map<String,Object> getMap(@RequestParam String key) {
        Set<Object> value = redisClient.sMembers(key);
        if (value != null && !value.isEmpty()) {
            Object object = value.iterator().next(); // 只有第一个元素，去第一个就可以
            if (object instanceof Map) {
                return (Map<String, Object>) object;
            }
        }
        return null;
    }

    // 获取map类型
    @GetMapping("/hGetAll")
    Object hGetAll(@RequestParam String key) {

        return redisClient.hGetAll(key);
    }

    // 获取map类型
    @GetMapping("/get")
    Object get(@RequestParam String key) {

        return redisClient.get(key);
    }

    // 获取指定字段的值
    @GetMapping("/getFieldValue")
    Object getFieldValue(@RequestParam String key, @RequestParam String field) {
        return redisClient.hGet(key, field);
    }

    // 获取set类型值
    @GetMapping("/getSMember")
    Set<Object> getSMember(@RequestParam String key) {
        return redisClient.sMembers(key);
    }

    /**
     * 使用管道传输数据
     * Redis管道（Pipeline）是一种客户端技术，用于将多个命令打包一次性发送给服务器，从而减少网络往返时间（RTT）
     * @param map
     */
    @PostMapping("/pipeline")
    public void pipeline(@RequestBody Map<String,String> map) {
        log.info("pipeline map大小:{}", map.size());
        redisClient.pipelined(operations -> {
            map.forEach((k,v)->{
                operations.opsForValue().set(k,v);
            });
        });
    }

    @PostMapping("/setAndInnerStr")
    Set<Object> setAndInnerStr(@RequestParam String key, @RequestParam String dirtyWordKey,@RequestBody String... values) {
        // 存储短信分词后的数据
        redisClient.sAdd(key,values);
        Set<Object> objects = redisClient.sIntersect(key, dirtyWordKey);
        redisClient.delete(key);
        return objects;
    }

    @PostMapping("/setValue")
    public void setValue(@RequestParam String key, @RequestParam Object value) {
        redisClient.set(key,value);
        log.info("setValue:{}",value);
    }

    @PostMapping("/setHMap")
    public void setHMap(@RequestParam String key,@RequestBody Map<String,Object> value) {
        redisClient.hSet(key,value);
        log.info("setMap:{}",value);
    }

    @PostMapping("/setMaps")
    public void setMap(@RequestParam String key,@RequestBody Map<String,Object> ... value) {
        redisClient.sAdd(key,value);

        log.info("setMap:{}",value);
    }

    @PostMapping("/setSStr")
    public void setSStr(@RequestParam String key,@RequestBody String ... value) {
        redisClient.sAdd(key,value);
        log.info("setSStr:{}",value);
    }
}
