package com.personal.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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
    @GetMapping("/cache/getMap")
    Map<String,Object> getMap(@RequestParam String key);

    @GetMapping("/cache/hGetAll")
    Object hGetAll(@RequestParam String key);

    @GetMapping("/cache/getFieldValue")
    Object getFieldValue(@RequestParam String key, @RequestParam String field);

    /** 重载 - 接收字符串类型
     * String类型可以支持【text/plain;chartset=utf-8】这个响应头, Object 不支持
     */
    @GetMapping("/cache/getFieldValue")
    String getFieldValueString(@RequestParam String key, @RequestParam String field);

    @GetMapping("/cache/getFieldValue")
    List<String> getFieldValueList(@RequestParam String key, @RequestParam String field);

    // 获取set类型值
    @GetMapping("/cache/getSMember")
    Set<Map> getSMember(@RequestParam String key);
}
