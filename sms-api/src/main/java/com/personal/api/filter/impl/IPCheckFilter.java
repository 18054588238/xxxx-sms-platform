package com.personal.api.filter.impl;

import com.personal.api.filter.ChainFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName IPCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验请求的ip地址是否是白名单
 */
@Service(value = "ip")
@Slf4j
public class IPCheckFilter implements ChainFilter {
    @Override
    public void check(Object obj) {
        log.info("[接口模块-校验 ip。。。]");
    }
}
