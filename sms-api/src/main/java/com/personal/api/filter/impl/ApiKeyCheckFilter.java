package com.personal.api.filter.impl;

import com.personal.Constant.CacheConstant;
import com.personal.api.feign.CacheFeignClient;
import com.personal.api.filter.ChainFilter;
import com.personal.enums.ExceptionEnums;
import com.personal.exception.ApiException;
import com.personal.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName ApiKeyCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验客户的apiKey是否合法
 */
@Service(value = "apikey") // 指定好服务名称，以通过配置文件实现动态校验
@Slf4j
public class ApiKeyCheckFilter implements ChainFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Override
    public void check(StandardSubmit submit) {
        log.info("[接口模块-校验apikey。。。]");
        String apikey = submit.getApikey();
        // 根据apikey查询redis，如果查不到，抛出异常，查到之后封装信息
        Map<String, Object> value = cacheFeignClient.getMap(CacheConstant.CLIENT_BUSINESS +apikey);

        if(value==null || value.isEmpty()){
            log.error("【接口模块-校验apikey】 非法的apikey = {}",apikey);
            throw new ApiException(ExceptionEnums.ERROR_APIKEY);
        }
        submit.setClientId(Long.parseLong(value.get("id") + ""));
        log.info("【接口模块-校验apikey】 查询到客户信息 value = {}",value);
    }
}
