package com.personal.api.filter.impl;

import com.personal.common.Constant.ApiConstant;
import com.personal.common.Constant.CacheConstant;
import com.personal.api.feign.CacheFeignClient;
import com.personal.api.filter.ChainFilter;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.ApiException;
import com.personal.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName SignCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验短信的签名
 */
@Slf4j
@Service(value = "sign")
public class SignCheckFilter implements ChainFilter {
    @Autowired
    private CacheFeignClient cacheFeignClient;

    /**
     * 截取签名的开始索引
     */
    private final int SIGN_START_INDEX = 1;

    /**
     * 客户存储签名信息的字段
     */
    private final String CLIENT_SIGN_INFO = "signInfo";
    private final String SIGN_ID = "id";

    @Override
    public void check(StandardSubmit submit) {
        log.info("[接口模块-校验 sign。。。]");
        String text = submit.getText();
        if (StringUtils.isBlank(text) || !text.startsWith(ApiConstant.SIGN_PREFIX) || !text.contains(ApiConstant.SIGN_SUFFIX)) {
            log.info("[接口模块-校验 sign 失败，text：{}]",text);
            throw new ApiException(ExceptionEnums.ERROR_SIGN);
        }
        String signText = text.substring(1, text.indexOf(ApiConstant.SIGN_SUFFIX));

        if (StringUtils.isBlank(signText)) {
            log.info("[接口模块-校验 sign 失败，text：{}]",text);
            throw new ApiException(ExceptionEnums.ERROR_SIGN);
        }
        Set<Map> valueSet = cacheFeignClient.getSMember(CacheConstant.CLIENT_SIGN + submit.getClientId());

        AtomicBoolean matchFlag = new AtomicBoolean(false);
        valueSet.forEach(map -> {
            if (map.get(CLIENT_SIGN_INFO).equals(signText)) {
                submit.setSign(signText);
                submit.setSignId(Long.valueOf(map.get(SIGN_ID)+""));
                matchFlag.set(true);
                return;
            }
        });

        if (!matchFlag.get()) {
            log.info("【接口模块-校验签名】   无可用签名 text = {}",text);
            throw new ApiException(ExceptionEnums.ERROR_SIGN);
        }
        log.info("[接口模块-校验 找到匹配的签名 sign = {}",signText);
    }
}
