package com.personal.api.filter.impl;

import com.personal.api.feign.CacheFeignClient;
import com.personal.api.filter.ChainFilter;
import com.personal.common.constants.CacheConstant;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.ApiException;
import com.personal.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IPCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验请求的ip地址是否是白名单
 */
@Service(value = "ip")
@Slf4j
public class IPCheckFilter implements ChainFilter {
    @Autowired
    private CacheFeignClient cacheFeignClient;

    private final String IP_ADDRESS_KEY = "ipAddress";

    @Override
    public void check(StandardSubmit submit) {


        List<String> ipList = cacheFeignClient.getFieldValueList(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), IP_ADDRESS_KEY);
        if (CollectionUtils.isEmpty(ipList) || ipList.contains(submit.getRealIP())) {
            submit.setIp(ipList);
            log.info("[接口模块-校验 ip通过]");
        } else {
            log.error("[接口模块-校验 请求的ip不在白名单内]");
            throw new ApiException(ExceptionEnums.IP_NOT_WHITE);
        }
    }
}
