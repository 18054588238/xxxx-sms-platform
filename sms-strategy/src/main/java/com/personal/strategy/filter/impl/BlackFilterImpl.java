package com.personal.strategy.filter.impl;

import com.personal.common.model.StandardSubmit;
import com.personal.strategy.filter.ChainFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChainFilterImpl
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 黑名单校验
 */
@Slf4j
@Service(value = "black")
public class BlackFilterImpl implements ChainFilter {
    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-黑名单校验】   校验ing…………");
    }
}
