package com.personal.management.config;

import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Author liupanpan
 * @Date 2026/1/26
 * @Description
 * 配置过滤器链、认证、授权
 */
@Configuration
public class ShiroConfig {

    Map<String, String> filterChainDefinitionMap = new LinkedHashMap();

    /* 自定义认证授权realm */
    @Bean
    public MyRealm myRealm() {
        return new MyRealm();
    }

    /*过滤器链*/
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        filterChainDefinitionMap.put("/sys/user/login","anon");// 放行
        filterChainDefinitionMap.put("/index.html","anon");// 放行
        filterChainDefinitionMap.put("/login.html","anon");// 放行
        filterChainDefinitionMap.put("/logout","anon");// 放行
        filterChainDefinitionMap.put("public/*","anon");// 放行
        filterChainDefinitionMap.put("/**","authc");// 认证

        chainDefinition.addPathDefinitions(filterChainDefinitionMap);

        return chainDefinition;
    }
    /*认证*/
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(this.myRealm());
        return securityManager;
    }
}
