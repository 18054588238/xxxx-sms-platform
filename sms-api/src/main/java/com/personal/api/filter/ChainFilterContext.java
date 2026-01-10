package com.personal.api.filter;

import com.personal.model.StandardSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName ChainFilterContext
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 用于获取ChainFilter接口的所有实现类
 */
@Component
@RefreshScope
public class ChainFilterContext {

    // Spring会自动将所有类型为ChainFilter的bean收集到一个Map中，其中键是bean的名称，值是bean的实例。
    @Autowired
    private Map<String,ChainFilter> chainFilterMap;

    // 从配置文件中获取key为"filters"的值，如果没有找到，则使用默认值"apikey,ip,sign,mobile,template,fee"
    @Value("${filters:apikey,ip,sign,mobile,template,fee}")
    private String filters;

    public void checkManagement(StandardSubmit submit) {
        String[] split = filters.split(",");
        for (String s : split) {
            ChainFilter chainFilter = chainFilterMap.get(s);
            chainFilter.check(submit);
        }
    }
}
