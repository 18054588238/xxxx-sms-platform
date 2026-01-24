package com.personal.gateway.runnable;

import com.personal.common.constants.CacheConstant;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.constants.SmsConstant;
import com.personal.common.enums.Cmpp2DeliverResultEnums;
import com.personal.common.enums.Cmpp2ResultEnums;
import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;
import com.personal.gateway.feign.CacheFeignClient;
import com.personal.gateway.netty4.entity.CmppDeliver;
import com.personal.gateway.netty4.entity.CmppSubmitResp;
import com.personal.gateway.netty4.utils.CmppReportMapUtil;
import com.personal.gateway.netty4.utils.CmppSubmitMapUtil;
import com.personal.gateway.netty4.utils.SpringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;

import static com.personal.common.constants.RabbitMQConstants.SMS_GATEWAY_NORMAL_EXCHANGE;

/**
 * @ClassName CmppDeliverRunnable
 * @Author liupanpan
 * @Date 2026/1/24
 * @Description 封装任务
 */
public class CmppDeliverRunnable implements Runnable {

    RabbitTemplate rabbitTemplate = SpringUtil.getBeanByClazz(RabbitTemplate.class);
    CacheFeignClient cacheFeignClient = SpringUtil.getBeanByClazz(CacheFeignClient.class);

    private long msgId;
    private String stat;


    public CmppDeliverRunnable() {
    }

    public CmppDeliverRunnable(long msgId, String stat) {
        this.msgId = msgId;
        this.stat = stat;
    }

    @Override
    public void run() {

        // 获取状态报告
        StandardReport report = CmppReportMapUtil.remove(msgId+"");

        if (StringUtils.isNotBlank(stat) && stat.equals(SmsConstant.DELIVRD)) {
            // 短信发送成功
            report.setReportState(SmsConstant.REPORT_STATE_SUCCESS);
        } else {
            report.setReportState(SmsConstant.REPORT_STATE_FAIL);
            report.setErrorMsg(Cmpp2DeliverResultEnums.getMsgByCode(stat));
        }
        // 根据用户选择，给用户返回状态报告 -- 将状态报告上传到rabbitmq中，由推送模块发送给客户
        // 从缓存中查询回调信息(策略模块只在校验失败时会封装是否回调，以及回调地址，而既然走到了短信网关模块，说明策略模块校验通过，也就没有封装回调信息 --- todo 其实这里可以从一开始就封装～)
        Integer isCallback = cacheFeignClient.getFieldValueInt(CacheConstant.CLIENT_BUSINESS + report.getApikey(), "isCallback");
        if (isCallback != null && isCallback == 1) {
            String callbackUrl = cacheFeignClient.getFieldValueString(CacheConstant.CLIENT_BUSINESS + report.getApikey(), "callbackUrl");
            if (StringUtils.isNotBlank(callbackUrl)) {
                report.setCallbackUrl(callbackUrl);
                report.setIsCallback(isCallback);
                // 发送状态报告到rabbitmq
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PUSH_REPORT,report);
            }
        }
        /**
         * 修改es中的submit的状态 -- 为了防止还没添加就修改的情况，将修改延迟执行
         * 发送消息，让[搜索模块]对之前写入的信息做修改，这里需要做一个死信队列，延迟10s发送修改es信息的消息
         * 声明好具体的交换机和队列后，直接发送report到死信队列即可
         */
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_GATEWAY_NORMAL_EXCHANGE,"",report);
    }
}
