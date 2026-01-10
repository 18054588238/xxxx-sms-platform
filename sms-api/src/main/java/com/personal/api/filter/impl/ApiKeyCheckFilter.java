package com.personal.api.filter.impl;

import com.personal.api.filter.ChainFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName ApiKeyCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验客户的apiKey是否合法
 */
@Service(value = "apikey") // 指定好服务名称，以通过配置文件实现动态校验
@Slf4j
public class ApiKeyCheckFilter implements ChainFilter {
    @Override
    public void check(Object obj) {
        log.info("[接口模块-校验apikey。。。]");
    }
}
