package com.personal.api.controller;

import com.personal.api.entity.req.SingleSendReq;
import com.personal.api.entity.res.R;
import com.personal.api.entity.res.ResultVO;
import com.personal.api.enums.SmsCodeEnum;
import com.personal.api.filter.ChainFilterContext;
import com.personal.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName SmsController
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description
 */
@RestController
@RequestMapping("/sms")
@Slf4j
public class SmsController {

    @Autowired
    private ChainFilterContext chainFilterContext;

    @Value("${headers}")
    private String headers;

    /**
     * 基于请求头获取信息时，可能获取到的未知信息
     */
    private final String UNKNOW = "unknow";

    /**
     * 如果是当前请求头获取IP地址，需要截取到第一个','未知
     */
    private final String X_FORWARDED_FOR = "x-forwarded-for";

    @PostMapping(value = "/single_send",produces = "application/json;charset=utf-8")
    public ResultVO singleSend(@RequestBody @Validated SingleSendReq req, BindingResult result, HttpServletRequest request) {

        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            log.info("【接口模块-单条短信Controller】 参数不合法 msg = {}",message);
            return R.error(SmsCodeEnum.PARAMETER_ERROR.getCode(),message);
        }
        // 获取客户端真实的IP地址
        String realIP = getRealIP(request);
        StandardSubmit submit = new StandardSubmit();

        submit.setRealIP(realIP);
        submit.setApikey(req.getApikey());
        submit.setMobile(req.getMobile());
        submit.setText(req.getText());
        submit.setUid(req.getUid());
        submit.setState(req.getState());

        return R.ok();
    }

    /**
     * 获取客户端真实的IP地址
     * @param request
     * @return
     */
    private String getRealIP(HttpServletRequest request) {
        String ip;
        for(String header:headers.trim().split(",")){
            ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip) && !UNKNOW.equalsIgnoreCase(ip)) {
                if (X_FORWARDED_FOR.equalsIgnoreCase(header) && ip.indexOf(",") > 0) {
                    ip = ip.substring(0, ip.indexOf(","));// X_FORWARDED_FOR中可能会包含多个ip，截取第一个ip
                }
                return ip;
            }
        }
        return request.getRemoteAddr();// 基于传统方式获取一个IP
    }
}
