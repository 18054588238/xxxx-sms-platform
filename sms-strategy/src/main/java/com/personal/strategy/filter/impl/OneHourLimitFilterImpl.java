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
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @ClassName OneMinuteLimitStrategyFilter
 * @Author liupanpan
 * @Date 2026/1/18
 * @Description 同一个客户平台，同一个手机号60s内只能发送一条
 */
//@Service(value = "oneHourLimit") todo
@Service
@Slf4j
public class OneHourLimitFilterImpl implements ChainFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;
    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    // 60s的毫秒值
    long hourMillis = 60 * 60 * 1000;
    private int LIMIT_COUNT = 3;

    @Override
    public void check(StandardSubmit submit) throws IOException {
        if (submit.getState() != SmsConstant.CODE_TYPE)
        log.info("【策略模块-短信发送1h限流校验】校验ing…………");

        String mobile = submit.getMobile();
        Long clientId = submit.getClientId();
        LocalDateTime sendTime = submit.getSendTime();

        long sendMillis = getLocalDateTimeMillis(sendTime);

        submit.setRetrySendTime(sendMillis);

        String zAddKey = CacheConstant.LIMIT_HOURS + clientId + CacheConstant.SEPARATE + mobile;

        // todo 并发操作时这里有问题，后续需要优化
        // 同一时间，多个并发
        int retryCount = 0;
        while (retryCount < LIMIT_COUNT && !cacheFeignClient.zAdd(zAddKey , submit.getRetrySendTime(), submit.getRetrySendTime())) {
            // 添加失败，执行重试机制
            submit.setRetrySendTime(getLocalDateTimeMillis(LocalDateTime.now()));
            retryCount++;
        }

        if (retryCount == LIMIT_COUNT) {
            // 1h内发送的短信数大于3了
            log.info("【策略模块-短信发送1h限流校验】操作频繁，1h内只能发送三条短信，请稍后发送～");
            // 只保留
            submit.setErrorMsg(ExceptionEnums.ONE_HOUR_LIMIT.getMessage()+",mobile:"+mobile);
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_HOUR_LIMIT);
        }
        // 1h区间 ： （最新发送时间-1h）—— 最新发送时间
        long min = submit.getRetrySendTime() - hourMillis;
        Long scoreCount = cacheFeignClient.getScoreCount(zAddKey, (double) min, (double) sendMillis);
        if (scoreCount > LIMIT_COUNT) {
            // 1h内发送的短信数大于3了
            log.info("【策略模块-短信发送1h限流校验】操作频繁，1h内只能发送三条短信，请稍后发送～");
            // todo 执行移除操作，只保留该时间范围内的前三条
            cacheFeignClient.zRemove(zAddKey, submit.getRetrySendTime());
            submit.setErrorMsg(ExceptionEnums.ONE_HOUR_LIMIT.getMessage()+",mobile:"+mobile);
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_HOUR_LIMIT);
        }
        log.info("【策略模块-短信发送1h限流校验】短信1h限流校验通过");
    }

    long getLocalDateTimeMillis(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
