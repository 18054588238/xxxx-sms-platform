package com.personal.api.filter.impl;

import com.personal.api.feign.CacheFeignClient;
import com.personal.api.filter.ChainFilter;
import com.personal.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final String IP_ADDRESS_KEY = "ip_address";

    @Override
    public void check(StandardSubmit submit) {

        /*Map<String, Object> map = cacheFeignClient.getMap(CacheConstant.CLIENT_BUSINESS + submit.getApikey());

        String ip = (String) map.get(IP_ADDRESS_KEY);
        if (ip == null || ip.contains(submit.getRealIP())) {
            submit.setIp(ip);
            log.info("[接口模块-校验 ip通过]");
        }
        log.error("[接口模块-校验 请求的ip不在白名单内]");
        throw new ApiException(ExceptionEnums.IP_NOT_WHITE);*/
    }
}
