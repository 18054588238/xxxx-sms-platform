package com.personal.api.filter.impl;

import com.personal.api.filter.ChainFilter;
import com.personal.api.utils.PhoneFormatCheckUtil;
import com.personal.enums.ExceptionEnums;
import com.personal.exception.ApiException;
import com.personal.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName MobileCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验手机号的格式合法性
 */
@Slf4j
@Service(value = "mobile")
public class MobileCheckFilter implements ChainFilter {
    @Override
    public void check(StandardSubmit submit) {
        if (PhoneFormatCheckUtil.checkPhoneFormat(submit.getMobile())) {
            log.info("[接口模块-校验 mobile 校验通过]");
        } else {
            log.error("[接口模块-校验 mobile 校验不通过]");
            throw new ApiException(ExceptionEnums.ERROR_MOBILE);
        }

    }
}
