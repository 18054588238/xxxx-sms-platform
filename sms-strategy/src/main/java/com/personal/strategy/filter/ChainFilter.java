package com.personal.strategy.filter;

import com.personal.common.model.StandardSubmit;

import java.io.IOException;

/**
 * @ClassName ChainFilter
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 校验
 */
public interface ChainFilter {
    void check(StandardSubmit submit) throws IOException;
}
