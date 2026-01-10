package com.personal.api.filter.impl;

import com.personal.api.filter.ChainFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName TemplateCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验短信的模板
 */
@Slf4j
@Service(value = "template")
public class TemplateCheckFilter implements ChainFilter {
    @Override
    public void check(Object obj) {
        log.info("[接口模块-校验 template。。。]");
    }
}
