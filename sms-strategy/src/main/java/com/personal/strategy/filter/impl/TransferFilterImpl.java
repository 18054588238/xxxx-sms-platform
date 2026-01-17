package com.personal.strategy.filter.impl;

import com.personal.common.constants.CacheConstant;
import com.personal.common.enums.MobileOperatorEnum;
import com.personal.common.model.StandardSubmit;
import com.personal.strategy.feign.CacheFeignClient;
import com.personal.strategy.filter.ChainFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChainFilterImpl
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 携号转网校验
 * 封装转网后的运营商
 */
@Slf4j
@Service(value = "transfer")
public class TransferFilterImpl implements ChainFilter {
    @Autowired
    private CacheFeignClient cacheFeignClient;

    private final Boolean IS_TRANSFER = true;

    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-携号转网校验】校验ing…………");
        String mobile = submit.getMobile();
        String result = cacheFeignClient.get(CacheConstant.TRANSFER + mobile);
        if (StringUtils.isNotBlank(result)) {
            // 说明该手机号属于 携号转网
            log.info("【策略模块-携号转网校验】该手机号属于 携号转网！,operator_id:{}",result);
            submit.setOperatorId(Integer.valueOf(result));
            submit.setIsTransfer(IS_TRANSFER);
            return;
        }
        log.info("【策略模块-携号转网校验】该手机号不属于携号转网！");
    }
}
