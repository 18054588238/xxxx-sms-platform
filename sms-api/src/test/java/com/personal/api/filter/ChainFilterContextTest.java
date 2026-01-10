package com.personal.api.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChainFilterContextTest {
    @Autowired
    ChainFilterContext chainFilterContext;
    @Test
    void contextLoads() {
        chainFilterContext.checkManagement(new Object());
    }
}