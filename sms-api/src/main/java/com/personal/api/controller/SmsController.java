package com.personal.api.controller;

import com.personal.api.entity.req.SingleSendReq;
import com.personal.api.entity.res.R;
import com.personal.api.entity.res.ResultVO;
import com.personal.api.enums.SmsCodeEnum;
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
public class SmsController {

    @PostMapping(value = "/single_send",produces = "application/json;charset=utf-8")
    public ResultVO singleSend(@RequestBody @Validated SingleSendReq req, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();

            String remoteAddr = request.getRemoteAddr();
            System.out.println(remoteAddr);
            return R.error(SmsCodeEnum.PARAMETER_ERROR.getCode(),message);
        }
        return R.ok();
    }
}
