package com.personal.test.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @ClassName CacheFeignClient
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description 调用缓存模块将数据存储到redis中
 */
@FeignClient("sms-cache")
public interface CacheFeignClient {
    @PostMapping("/cache/setValue")
    void setValue(@RequestParam String key, @RequestParam Object value);

    @PostMapping("/cache/setHMap")
    public void setHMap(@RequestParam String key,@RequestBody Map<String,Object> value);

    @PostMapping("/cache/setMaps")
    void setMap(@RequestParam String key,@RequestBody Map<String,Object> ...value);

    @PostMapping("/cache/pipeline")
    void pipeline(@RequestBody Map<String,String> map);

    @PostMapping("/cache/setSStr")
    void setSStr(@RequestParam String key,@RequestBody String ... value);
}
