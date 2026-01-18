package com.personal.strategy.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @ClassName CacheFeignClient
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description 调用缓存模块
 */
@FeignClient("sms-cache")
public interface CacheFeignClient {
    // 获取set类型值
    @GetMapping("/cache/getSMember")
    Set<Map> getSMember(@RequestParam String key);

    @GetMapping("/cache/getSMember")
    Set<String> getSMemberStr(@RequestParam String key);

    /**
     * openFeign 进行类型转换 Object -> String
     */
    @GetMapping("/cache/getFieldValue")
    String getFieldValueString(@RequestParam String key, @RequestParam String field);

    @GetMapping("/cache/getFieldValue")
    Integer getFieldValueInt(@RequestParam String key, @RequestParam String field);

    @GetMapping("/cache/get")
    String get(@RequestParam String key);

    @PostMapping("/cache/setAndInnerStr")
    Set<Object> setAndInnerStr(@RequestParam String key, @RequestParam String dirtyWordKey,@RequestBody String... values);

    @PostMapping("/cache/zAdd")
    Boolean zAdd(@RequestParam String key, @RequestParam Object member,@RequestParam Long score);

    @GetMapping("/cache/getScoreCount")
    Long getScoreCount(@RequestParam String key,@RequestParam Double minScore,@RequestParam Double maxScore);

    @DeleteMapping("/cache/zRemoveRange")
    public void zRemoveRange(@RequestParam String key, @RequestParam long start,@RequestParam long end);

    @DeleteMapping("/cache/zRemove")
    public void zRemove(@RequestParam String key,@RequestParam Object member);

    @GetMapping("/cache/getFeeIncreBy")
    public Long getFeeWithIncrementBy(@RequestParam String hashMapName,
                                      @RequestParam String key,
                                      @RequestParam long delta);
}
