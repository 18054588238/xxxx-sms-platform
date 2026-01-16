package com.personal.strategy.utils;

import com.personal.common.constants.CacheConstant;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.constants.SmsConstant;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;
import com.personal.common.utils.MapStructUtil;
import com.personal.strategy.feign.CacheFeignClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ErrorSendMsgUtil
 * @Author liupanpan
 * @Date 2026/1/16
 * @Description
 */
@Component
public class ErrorSendMsgUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MapStructUtil mapStructUtil;
    @Autowired
    private CacheFeignClient cacheFeignClient;

    public void sendPushReport(StandardSubmit submit) {
        // 从缓存中查询回调信息
        Integer isCallback = cacheFeignClient.getFieldValueInt(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), "isCallback");
        if (isCallback != null && isCallback == 1) {
            String callbackUrl = cacheFeignClient.getFieldValueString(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), "callbackUrl");
            if (StringUtils.isNotBlank(callbackUrl)) {
                StandardReport standardReport = mapStructUtil.getStandardReport(submit);
                standardReport.setCallbackUrl(callbackUrl);
                standardReport.setIsCallback(isCallback);
                // 发送状态报告到rabbitmq
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PUSH_REPORT,standardReport);
            }
        }
    }

    public void sendWriteLog(StandardSubmit submit, List<String> dirtyWords) {
        // 校验失败，发送消息到rabbitmq，记录日志
        submit.setReportState(SmsConstant.REPORT_STATE_FAIL);
        submit.setErrorMsg(ExceptionEnums.HAS_DIRTY_WORD.getMessage()+",dirtyWords ="+ dirtyWords.toString());

        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_WRITE_LOG, submit);
    }
}
