package com.personal.management.controller;

import com.personal.common.enums.ExceptionEnums;
import com.personal.common.utils.R;
import com.personal.common.vo.ResultVO;
import com.personal.management.entity.ClientBusiness;
import com.personal.management.entity.SmsMenu;
import com.personal.management.entity.SmsUser;
import com.personal.management.entity.vo.ClientBusinessVO;
import com.personal.management.entity.vo.SearchSmsVO;
import com.personal.management.feign.SearchFeignClient;
import com.personal.management.service.ClientBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SmsManageController
 * @Author liupanpan
 * @Date 2026/1/28
 * @Description
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class SmsManageController {

    @Autowired
    protected ClientBusinessService clientBusinessService;

    @Autowired
    private SearchFeignClient searchFeignClient;

    /* 获取当前用户权限下所有客户 */
    @GetMapping("/clientbusiness/all")
    public ResultVO queryAllClientBusiness(){
        SmsUser smsUser = getSmsUser();

        if(smsUser == null){
            log.info("【获取登录用户信息】 用户未登录！！");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }
        List<ClientBusiness> clientBusinesses = clientBusinessService.queryAllClientBusiness(smsUser.getId());

        List<ClientBusinessVO> data = clientBusinesses.stream().map(clientBusiness -> {
            ClientBusinessVO clientBusinessVO = new ClientBusinessVO();
            try {
                org.springframework.beans.BeanUtils.copyProperties(clientBusiness, clientBusinessVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return clientBusinessVO;
        }).collect(Collectors.toList());

        return R.ok(data);
    }

    // ?size=10&from=0&content=&starttime=&stoptime=&mobile=&clientID=1
    @GetMapping("/search/list")
    public ResultVO searchList(@RequestParam Map map){
        SmsUser smsUser = getSmsUser();
        if(smsUser == null){
            log.info("【获取登录用户信息】 用户未登录！！");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }
        // 当前用户权限下的所有客户
        List<Long> clientIds = clientBusinessService.queryAllClientBusiness(smsUser.getId()).stream()
                .map(ClientBusiness::getId).collect(Collectors.toList());

        String clientIDStr = (String) map.get("clientID");
        if (StringUtils.isBlank(clientIDStr)) {
            // 没有传clientID，则默认筛选出当前用户权限下所有客户
            map.put("clientID",clientIds);
        } else {
            // 判断前端传递的clientID是否在用户权限内，不在权限内抛出异常
            Long clientID = Long.parseLong(clientIDStr);
            if (!clientIds.contains(clientID)) {
                log.info("【搜索短信信息】 用户权限不足,没有该客户的权限！");
                return R.error(ExceptionEnums.SMS_NO_AUTHOR);
            }
        }

        //3. 发送请求到搜索模块-es检索，返回total和rows
        Map<String, Object> data = searchFeignClient.findSmsByParameters(map);
        //4. 响应结果
        //3、判断返回的total，如果total为0，正常返回
        Long total = Long.parseLong(data.get("total") + "");

        //4、如果数据正常，做返回数据的封装，声明SearchSmsVO的实体类，
        List<Map> list = (List<Map>) data.get("rows");
        List<SearchSmsVO> rows = new ArrayList<>();
        // 遍历集合，封装数据
        for (Map row : list) {
            SearchSmsVO vo = new SearchSmsVO();
            try {
                BeanUtils.copyProperties(vo,row);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rows.add(vo);
        }

        return R.ok(total,rows);
    }

    private static SmsUser getSmsUser() {
        Subject subject = SecurityUtils.getSubject();
        return (SmsUser) subject.getPrincipal();
    }
}
