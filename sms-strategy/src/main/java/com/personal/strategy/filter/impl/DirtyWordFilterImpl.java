package com.personal.strategy.filter.impl;

import com.personal.common.model.StandardSubmit;
import com.personal.strategy.filter.ChainFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChainFilterImpl
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 敏感词校验
 */
@Slf4j
@Service(value = "dirtyword")
public class DirtyWordFilterImpl implements ChainFilter {
    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-敏感词校验】   校验ing…………");
    }
}
