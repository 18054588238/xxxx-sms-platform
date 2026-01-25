package com.personal.monitor.controller;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Author liupanpan
 * @Date 2026/1/25
 * @Description
 */
@RestController
public class TestController {

    @XxlJob(value = "demoJobHandler")
    public void testXxlJob() {
        XxlJobHelper.log("xxl-job-demoJobHandler......log");
        System.out.println("ok执行.......");
    }
}
