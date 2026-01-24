package com.personal.gateway.netty4.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName SpringUtil
 * @Author liupanpan
 * @Date 2026/1/15
 * @Description 获取spring容器中的对象
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static Object getBeanByName(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBeanByClazz(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
