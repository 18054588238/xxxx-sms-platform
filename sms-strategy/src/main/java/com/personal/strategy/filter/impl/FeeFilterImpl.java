package com.personal.strategy.filter.impl;

import com.personal.common.constants.CacheConstant;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.StrategyException;
import com.personal.common.model.StandardSubmit;
import com.personal.strategy.feign.CacheFeignClient;
import com.personal.strategy.filter.ChainFilter;
import com.personal.strategy.utils.ClientBalanceUtil;
import com.personal.strategy.utils.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChainFilterImpl
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 路由校验
 */
@Slf4j
@Service(value = "fee")
public class FeeFilterImpl implements ChainFilter {
    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    private final String BALANCE = "balance";

    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-扣费校验】校验ing…………");

        // 当前短信需要的费用
        Long fee = submit.getFee();
        Long clientId = submit.getClientId();

        // 查询redis缓存中当前用户扣除后的费用
        Long feeWithIncrementBy = cacheFeignClient.getFeeWithIncrementBy(CacheConstant.CLIENT_BALANCE + clientId, BALANCE, -fee);
        Long amountLimit = ClientBalanceUtil.getClientAmountLimit(clientId);
        if (feeWithIncrementBy < amountLimit){
            // 把扣除的钱再加回去
            Long curFee = cacheFeignClient.getFeeWithIncrementBy(CacheConstant.CLIENT_BALANCE + clientId, BALANCE, fee);
            // 欠费额度超出限制
            log.info("【策略模块-扣费校验】短信发送失败，欠费额度超出限制，请充值后发送,当前费用：{}",curFee);
            submit.setErrorMsg(ExceptionEnums.BALANCE_NOT_ENOUGH.getMessage()+",当前余额："+curFee);
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.BALANCE_NOT_ENOUGH);
        }
        log.info("【策略模块-扣费校验】校验成功，已扣除短信费用,当前费用：{}",feeWithIncrementBy);
    }
}
