package com.personal.api.filter.impl;

import com.personal.api.filter.ChainFilter;
import com.personal.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName TemplateCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验客户剩余的金额是否充足
 */
@Slf4j
@Service(value = "fee")
public class FeeCheckFilter implements ChainFilter {
    @Override
    public void check(StandardSubmit submit) {
        log.info("[接口模块-校验 fee。。。]");
    }
}
