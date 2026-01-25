package com.personal.monitor.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailUtilTest {

    @Test
    void sendMail() {
        String receiveMail = "123";
        String[] split = receiveMail.split(",");
        System.out.println(split[0]);
    }
}