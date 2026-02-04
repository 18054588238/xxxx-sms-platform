package com.personal.management.service.impl;

import com.alibaba.fastjson.JSON;
import com.personal.management.entity.SmsUser;
import com.personal.management.service.SmsUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmsUserServiceImplTest {

    @Autowired
    private SmsUserService userService;
    @Test
    void test() {
        SmsUser smsUser = userService.queryById(1l);
        System.out.println(JSON.toJSONString(smsUser));
    }
}