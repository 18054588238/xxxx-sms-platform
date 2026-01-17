package com.personal.strategy.filter.impl;

import com.personal.common.constants.CacheConstant;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.StrategyException;
import com.personal.common.model.StandardSubmit;
import com.personal.strategy.feign.CacheFeignClient;
import com.personal.strategy.filter.ChainFilter;
import com.personal.strategy.utils.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChainFilterImpl
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 黑名单校验
 */
@Slf4j
@Service(value = "blackClient")
public class BlackClientFilterImpl implements ChainFilter {
    @Autowired
    private CacheFeignClient cacheFeignClient;
    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    private final String TRUE = "1";

    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-黑名单校验】客户级黑名单校验ing…………");
        String mobile = submit.getMobile();
        String result = cacheFeignClient.get(CacheConstant.MOBILE_BLACK +submit.getClientId()+CacheConstant.SEPARATE+ mobile);
        if (TRUE.equals(result)) {
            // 该手机号是黑名单
            log.info("【策略模块-黑名单校验】校验失败，该手机号是: 客户级黑名单,mobile:{}",mobile);
            // 校验失败，发送消息
            submit.setErrorMsg(ExceptionEnums.BLACK_CLIENT.getMessage()+",mobile:"+mobile);
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.BLACK_CLIENT);
        }
        log.info("【策略模块-黑名单校验】客户级黑名单校验成功");
    }
}
