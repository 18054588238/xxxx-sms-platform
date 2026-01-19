package com.personal.strategy.filter.impl;

import com.personal.common.constants.CacheConstant;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.StrategyException;
import com.personal.common.model.StandardSubmit;
import com.personal.strategy.feign.CacheFeignClient;
import com.personal.strategy.filter.ChainFilter;
import com.personal.strategy.utils.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personal.strategy.utils.ChannelTransferUtil;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @ClassName ChainFilterImpl
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 路由校验
 */
@Slf4j
@Service(value = "route")
public class RouteFilterImpl implements ChainFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;
    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-路由校验】   校验ing…………");
        Long clientId = submit.getClientId();
        // 获取客户关联的所有通道信息
        Set<Map> sMember = cacheFeignClient.getSMember(CacheConstant.CLIENT_CHANNEL + clientId);
        // Set<Map> 根据map中的clientChannelWeight降序排序
        TreeSet<Map> mapTreeSet = new TreeSet<>((o1, o2) -> {
            Integer o1weight = (Integer) o1.get("clientChannelWeight");
            Integer o2weight = (Integer) o2.get("clientChannelWeight");
            return o2weight.compareTo(o1weight);
        });
        mapTreeSet.addAll(sMember);

        boolean isSuccess = false;

        Map selectedChannel;

        for (Map map : mapTreeSet) {
            // 判断是否可用
            if (!((Boolean) map.get("available"))) {
                // 该通道不可用，校验下一个通道
                continue;
            }

            // 查询通道的详细信息
            Map channelMap = cacheFeignClient.hGetAll(CacheConstant.CHANNEL + map.get("channelId"));
            // 判断通道关联的运营商和客户的运营商是否相等
            Integer channelType = (Integer) channelMap.get("channelType");
            if ( channelType != 0 && !channelType.equals(submit.getOperatorId())) {
                // 该通道不可用，校验下一个通道
                continue;
            }
            // 如果后期涉及到的通道的转换，这里留一个口子
            selectedChannel = ChannelTransferUtil.transfer(submit, channelMap);

            // 校验通过
            isSuccess = true;
        }
        if (!isSuccess) {
            log.info("【策略模块-路由校验】当前客户没有匹配的路由通道，client_id = {}",clientId);
            submit.setErrorMsg(ExceptionEnums.NO_ROUTE_CHANNEL.getMessage()+"client_id =" + clientId);
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.NO_ROUTE_CHANNEL);
        }
    }
}
