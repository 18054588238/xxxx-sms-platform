package com.personal.strategy.filter.impl;

import com.personal.common.constants.CacheConstant;
import com.personal.common.constants.SmsConstant;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.StrategyException;
import com.personal.common.model.StandardSubmit;
import com.personal.strategy.feign.CacheFeignClient;
import com.personal.strategy.filter.ChainFilter;
import com.personal.strategy.utils.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @ClassName OneMinuteLimitStrategyFilter
 * @Author liupanpan
 * @Date 2026/1/18
 * @Description 同一个客户平台，同一个手机号60s内只能发送一条
 */
//@Service
@Service(value = "oneMinuteLimit")
@Slf4j
public class OneMinuteLimitFilterImpl implements ChainFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;
    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    // 60s的毫秒值
    long sixtyMillis = 60 * 1000;

    @Override
    public void check(StandardSubmit submit) throws IOException {
        if (submit.getState() != SmsConstant.CODE_TYPE) {
            return;
        }
        log.info("【策略模块-短信发送60s限流校验】60s校验ing…………");

        String mobile = submit.getMobile();
        Long clientId = submit.getClientId();
        // LocalDateTime本身并不包含时区信息，因此它不能直接转换为时间戳（毫秒值）。
        // 要获取毫秒值，我们需要将LocalDateTime转换为一个带有时区信息的对象，
        // 通常是通过将其转换为Instant，然后获取毫秒值
        LocalDateTime sendTime = submit.getSendTime();

        long sendMillis = sendTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        String zAddKey = CacheConstant.LIMIT_MINUTES + clientId + CacheConstant.SEPARATE + mobile;
        Boolean b = cacheFeignClient.zAdd(zAddKey , sendMillis, sendMillis);
        if (!b) {
            // member值相同，添加失败，会更新score值，说明存在并发操作
            log.info("【策略模块-短信发送60s限流校验】操作频繁，60s内只能发送一条短信，请稍后发送～");
            submit.setErrorMsg(ExceptionEnums.ONE_MINUTE_LIMIT.getMessage()+",mobile:"+mobile);
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_MINUTE_LIMIT);
        }
        // 60s区间 ： （发送时间-60s）—— 发送时间
        long min = sendMillis - sixtyMillis; // 不需要-1 ，查询操作是闭区间
        Long scoreCount = cacheFeignClient.getScoreCount(zAddKey, (double) min, (double) sendMillis);
        if (scoreCount > 1) {
            // 说明60s内发送的短信数大于1了
            log.info("【策略模块-短信发送60s限流校验】操作频繁，60s内只能发送一条短信，请稍后发送～");
            // 移除，该时间范围内只保留1条
            cacheFeignClient.zRemove(zAddKey, sendMillis);
            submit.setErrorMsg(ExceptionEnums.ONE_MINUTE_LIMIT.getMessage()+",mobile:"+mobile);
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_MINUTE_LIMIT);
        }
        log.info("【策略模块-短信发送60s限流校验】短信60s限流校验通过");
    }
}
