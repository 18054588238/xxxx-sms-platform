package com.personal.gateway.runnable;

import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.constants.SmsConstant;
import com.personal.common.enums.Cmpp2ResultEnums;
import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;
import com.personal.gateway.netty4.entity.CmppSubmitResp;
import com.personal.gateway.netty4.utils.CmppReportMapUtil;
import com.personal.gateway.netty4.utils.CmppSubmitMapUtil;
import com.personal.gateway.netty4.utils.SpringUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;

/**
 * @ClassName CmppSubmitRunnable
 * @Author liupanpan
 * @Date 2026/1/24
 * @Description 封装任务
 */
public class CmppSubmitRunnable implements Runnable {

    RabbitTemplate rabbitTemplate = SpringUtil.getBeanByClazz(RabbitTemplate.class);

    private CmppSubmitResp resp;

    public CmppSubmitRunnable(CmppSubmitResp resp) {
        this.resp = resp;
    }

    public CmppSubmitRunnable() {
    }

    @Override
    public void run() {
        StandardReport report = null;

        // 1.根据SequenceId获取临时存储的submit信息
        StandardSubmit submit = CmppSubmitMapUtil.remove(resp.getSequenceId());
        // 2. 获取运营商响应状态
        int result = resp.getResult();
        if (result != SmsConstant.OPERATOR_STATE_SUCCESS) {
            // 说明运营商的提交应答中回馈的失败的情况
            submit.setReportState(SmsConstant.REPORT_STATE_FAIL);
            submit.setErrorMsg(Cmpp2ResultEnums.getMsgByCode(result));
        } else {
            // 3.将submit封装为Report，临时存储，以便运营商第二次响应返回状态码时，可以再次获取到信息
            report = new StandardReport();
            BeanUtils.copyProperties(submit, report);
            // 临时存储
            CmppReportMapUtil.put(resp.getMsgId()+"", report);
        }
        //4、将封装好的submit直接扔RabbitMQ中，让搜索模块记录信息
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_WRITE_LOG,submit);
    }
}
