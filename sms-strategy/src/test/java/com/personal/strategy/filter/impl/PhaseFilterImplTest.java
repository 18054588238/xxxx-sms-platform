package com.personal.strategy.filter.impl;

import com.personal.common.model.StandardSubmit;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PhaseFilterImplTest {

    @Test
    void check() {
        String s = "18054588238";
        System.out.println(StringUtils.substring(s, 0, 7));
    }
}