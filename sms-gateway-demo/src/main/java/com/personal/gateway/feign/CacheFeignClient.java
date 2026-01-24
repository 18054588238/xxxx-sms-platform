package com.personal.gateway.feign;

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

    /**
     * openFeign 进行类型转换 Object -> String
     */
    @GetMapping("/cache/getFieldValue")
    String getFieldValueString(@RequestParam String key, @RequestParam String field);

    @GetMapping("/cache/getFieldValue")
    Integer getFieldValueInt(@RequestParam String key, @RequestParam String field);

}
