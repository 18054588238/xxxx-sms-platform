package com.personal.management.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @ClassName SearchFeignClient
 * @Author liupanpan
 * @Date 2026/1/29
 * @Description
 */
@FeignClient("sms-search")
public interface SearchFeignClient {
    @PostMapping("/search/findSmsByParameters")
    Map<String, Object> findSmsByParameters(@RequestBody Map map);
}
