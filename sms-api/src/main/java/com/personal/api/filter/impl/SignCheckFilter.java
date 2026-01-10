package com.personal.api.filter.impl;

import com.personal.api.filter.ChainFilter;
import com.personal.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName SignCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验短信的签名
 */
@Slf4j
@Service(value = "sign")
public class SignCheckFilter implements ChainFilter {
    @Override
    public void check(StandardSubmit submit) {
        log.info("[接口模块-校验 sign。。。]");
    }
}
