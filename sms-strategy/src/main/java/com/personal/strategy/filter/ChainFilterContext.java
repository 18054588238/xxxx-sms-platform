package com.personal.strategy.filter;

import com.personal.common.constants.ApiConstant;
import com.personal.common.constants.CacheConstant;
import com.personal.common.model.StandardSubmit;
import com.personal.strategy.feign.CacheFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @ClassName ChainFilterContext
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description
 */
@Component
@Slf4j
public class ChainFilterContext {

    @Autowired
    private Map<String,ChainFilter> chainFilterMap;

    @Autowired
    private CacheFeignClient cacheFeignClient;

    private final String CLIENT_FILTERS = "clientFilters";

    public void checkManagement(StandardSubmit submit) {
        // 从缓存中获取需要校验的信息
        Set<Map> sMember = cacheFeignClient.getSMember(CacheConstant.CLIENT_BUSINESS + submit.getApikey());
        if (Objects.isNull(sMember) || sMember.isEmpty()) {
            return;
        }
        String checkWords = "";
        for (Map map : sMember) {
            checkWords = (String) map.get(CLIENT_FILTERS);
        }

        if (StringUtils.isNotBlank(checkWords)) {
            String[] words = checkWords.split(",");
            for (String word : words) {
                ChainFilter chainFilter = chainFilterMap.get(word);
                if (Objects.nonNull(chainFilter)) {
                    try {
                        chainFilter.check(submit);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
