package com.personal.api.filter;

import com.personal.common.model.StandardSubmit;

/**
 * @ClassName ChainFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 用于校验的父接口，策略模式的父接口
 * 接口模块核心的事情就是对当前的请求参数和一些信息做校验。
 * 校验方式 做到动态可插拔，后期的 扩展性 可以更好。
 * 使用 策略模式 + 责任链模式。
 * 向上声明一个接口，内部提供好校验方法。在执行校验时，以责任链的形式一个一个去校验。
 * 基于Nacos提供的一个配置的 动态刷新 去实现，指定好整体校验链，需要改变时，只需要去修改Nacos中的配置文件即可。
 */
public interface ChainFilter {
    void check(StandardSubmit submit);
}
