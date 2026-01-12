package com.personal.api.filter.impl;

import com.personal.common.constants.ApiConstant;
import com.personal.common.constants.CacheConstant;
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

/**
 * @ClassName TemplateCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验短信的模板
 */
@Slf4j
@Service(value = "template")
public class TemplateCheckFilter implements ChainFilter {
    @Autowired
    private CacheFeignClient cacheFeignClient;

    private final String TEMPLATE_TEXT = "templateText";

    private final String TEMPLATE_PLACEHOLDER = "#";

    @Override
    public void check(StandardSubmit submit) {
        String text = submit.getText();
        Set<Map> sMember = cacheFeignClient.getSMember(CacheConstant.CLIENT_TEMPLATE + submit.getSignId());

        if(sMember==null||sMember.isEmpty() || StringUtils.isBlank(text)){
            log.error("[接口模块-校验 template 校验失败]");
            throw new ApiException(ExceptionEnums.ERROR_TEMPLATE);
        }
        // 去掉签名后的短信内容
        String lastText = StringUtils.substringAfter(text, ApiConstant.SIGN_SUFFIX);
        boolean matchFlag = false;

        for (Map map : sMember) {
            String templateText = map.get(TEMPLATE_TEXT).toString(); // 模版内容
            if (StringUtils.equals(templateText, lastText)) {
                matchFlag = true;
                return;
            }
            // 只包含两个#
            int countMatches = StringUtils.countMatches(templateText, TEMPLATE_PLACEHOLDER);
            if (StringUtils.isNotBlank(templateText) && templateText.contains(TEMPLATE_PLACEHOLDER) && countMatches == 2) {
                // 去掉模版中的占位符
                String templatePrefix = StringUtils.substringBefore(templateText, TEMPLATE_PLACEHOLDER);
                String templateSuffix = StringUtils.substringAfterLast(templateText, TEMPLATE_PLACEHOLDER);
                if (lastText.startsWith(templatePrefix) && lastText.endsWith(templateSuffix)) {
                    matchFlag = true;
                    return;
                }
            }
        }
        if (!matchFlag) {
            log.error("[接口模块-校验 template 校验失败]");
            throw new ApiException(ExceptionEnums.ERROR_TEMPLATE);
        } else {
            log.info("[接口模块-校验 template 校验成功]");
        }
    }
}
