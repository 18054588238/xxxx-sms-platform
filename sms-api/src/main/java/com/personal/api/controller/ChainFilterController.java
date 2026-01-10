package com.personal.api.controller;

import com.personal.api.filter.ChainFilterContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ChainFilterController
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description
 */
@Slf4j
@RestController
public class ChainFilterController {
    @Autowired
    private ChainFilterContext chainFilterContext;

    @PostMapping("/check")
    public void checkManagement(Object obj){
        chainFilterContext.checkManagement(obj);
    }
}
