package com.personal.strategy.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * openFeign 进行类型转换 Object -> String
     */
    @GetMapping("/cache/getFieldValue")
    String getFieldValueString(@RequestParam String key, @RequestParam String field);

    @GetMapping("/cache/get")
    String get(@RequestParam String key);
}
