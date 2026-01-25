package com.personal.monitor.feign;

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
    @GetMapping("/cache/getScanKeys")
    Set<String> getScanKeys();
}
