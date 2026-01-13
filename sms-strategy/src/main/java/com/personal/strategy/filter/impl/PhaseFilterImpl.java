package com.personal.strategy.filter.impl;

import com.personal.common.constants.CacheConstant;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.model.StandardSubmit;
import com.personal.common.utils.OperatorUtil;
import com.personal.strategy.config.RabbitMQConfig;
import com.personal.strategy.feign.CacheFeignClient;
import com.personal.strategy.filter.ChainFilter;
import com.personal.strategy.utils.MobileOperatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChainFilterImpl
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 号段补全校验
 */
@Slf4j
@Service(value = "phase")
public class PhaseFilterImpl implements ChainFilter {

    /**
     * 切分手机号前7位
     */
    private final int MOBILE_START = 0;
    private final int MOBILE_END = 7;
    /**
     * 校验的长度
     */
    private final int LENGTH = 2;
    /**
     * 分割区域和运营商的标识
     */
    private final String SEPARATE = ",";
    /**
     * 未知的情况
     */
    private final String UNKNOWN = "未知 未知,未知";

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Autowired
    private MobileOperatorUtil mobileOperatorUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-号段补齐校验】   校验ing…………");
        // 获取手机号前七位
        String mobile = StringUtils.substring(submit.getMobile(), MOBILE_START, MOBILE_END);
        // 查询redis
        String mobileInfo = cacheFeignClient.get(CacheConstant.MOBILE_PHASE +mobile);
        if (StringUtils.isBlank(mobileInfo)) {
            // 调用第三方接口查询
            mobileInfo = mobileOperatorUtil.getMobileOperator(mobile);
            // 如果查到了发送消息到mq，其他模块监听，将其保存到mysql 及redis中
            rabbitTemplate.convertAndSend(RabbitMQConstants.MOBILE_AREA_OPERATOR,mobileInfo+SEPARATE+mobile);
        }
        if (StringUtils.isBlank(mobileInfo)) {
            mobileInfo = UNKNOWN;
        }

        String[] mobileSplit = mobileInfo.split(SEPARATE);
        if (mobileSplit.length == LENGTH) {
            submit.setArea(mobileSplit[0]);
            submit.setOperatorId(OperatorUtil.getOperatorId(mobileSplit[1]));
        }
    }
}
