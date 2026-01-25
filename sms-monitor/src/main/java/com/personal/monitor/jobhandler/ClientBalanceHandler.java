package com.personal.monitor.jobhandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @ClassName XxlJobHandler
 * @Author liupanpan
 * @Date 2026/1/25
 * @Description 监控客户余额
 */
@Component
public class ClientBalanceHandler {

    @XxlJob("clientBalanceHandler")
    public void monitor() {

    }


}
