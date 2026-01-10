package com.personal.api.filter.impl;

import com.personal.api.filter.ChainFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName MobileCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验手机号的格式合法性
 */
@Slf4j
@Service(value = "mobile")
public class MobileCheckFilter implements ChainFilter {
    @Override
    public void check(Object obj) {
        log.info("[接口模块-校验 mobile。。。]");
    }
}
