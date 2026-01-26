package com.personal.cache.controller;

import com.msb.framework.redis.RedisClient;
import com.personal.cache.model.TestUser;
import com.personal.common.constants.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.List;
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

    // hash结构 一次获取多个字段的值 - 返回的是按顺序对应的字段值
    @PostMapping("/hMultiGet")
    public List<Object> hMultiGet(@RequestParam String key, @RequestBody String[] fields) throws InterruptedException {
        List<Object> values = redisClient.hMultiGet(key, fields);
        log.info("查询成功，values:{}",values);
        return values;
    }

    // 获取redis中存储的key
    @GetMapping("/getScanKeys")
    public Set<String> getScanKeys(@RequestParam String pattern) throws InterruptedException {
        // todo 这里查询太慢了，后期可以优化 client_id 的存储方式或者使用异步方式等
        Set<String> scanKeys = redisClient.scan(pattern);
        log.info("查询成功，scanKeys:{}",scanKeys);
        return scanKeys;
    }

    @GetMapping("/getFeeIncreBy")
    public Long getFeeWithIncrementBy(@RequestParam String hashMapName,
                                      @RequestParam String key,
                                      @RequestParam long delta) {
        log.info("【缓存模块】 hIncrBy方法，自增   key = {},field = {}，number = {}", hashMapName,key,delta);
        Long fee = redisClient.hIncrementBy(hashMapName, key, delta);
        log.info("更新后 fee = {}",fee);
        return fee;
    }

    @DeleteMapping("/zRemoveRange")
    public void zRemoveRange(@RequestParam String key, @RequestParam long start,@RequestParam long end) {
        log.info("zRemoveRange,有序集合的key:{},移除范围{}-{}",key,start,end);
        // zSet默认是按照分数（score）升序排列的。
        redisClient.zRemoveRange(key,start,end);
    }

    @DeleteMapping("/zRemove")
    public void zRemove(@RequestParam String key,@RequestParam Object member) {
        log.info("zRemove,有序集合的key:{},移除值{}",key,member);
        redisClient.zRemove(key,member);
    }

    /**
     * 使用zSet结构实现短信限流操作，key：客户标记和手机号，value和score：当前系统时间的毫秒值
     * score 可以重复，但成员不能重复
     */
    @PostMapping("/zAdd")
    public Boolean zAdd(@RequestParam String key, @RequestParam Object member,@RequestParam Long score) {
        log.info("zAdd,有序集合的key:{},要添加的成员member:{},成员的分数score:{}",key,member,score);
        // 将成员v及其分数score添加到键为key的有序集合中。如果成员已存在，则更新其分数。
        return redisClient.zAdd(key,member,score);
    }

    /**
     * 使用zSet结构实现短信限流操作，例如：限制1min只能发一条，如果在1min的score区间中查询到了2条数据，则不允许发送
     */
    @GetMapping("/getScoreCount")
    public Long getScoreCount(@RequestParam String key,@RequestParam Double minScore,@RequestParam Double maxScore) {
        // 统计ZSet（有序集合）中分数在指定范围内的成员数量的方法,默认包含边界
        return redisClient.zCount(key,minScore,maxScore); // 如果key不存在，count会返回0
    }

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
