package com.personal.enums;

import lombok.Getter;

/**
 * @ClassName ExceptionEnums
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description
 */
@Getter
public enum ExceptionEnums {
    ERROR_APIKEY(-1,"非法的apikey"),
    IP_NOT_WHITE(-2,"请求的ip不在白名单内"),
    ERROR_SIGN(-3,"无可用签名"),
    ERROR_TEMPLATE(-4,"无可用模板"),
    ERROR_MOBILE(-5,"手机号格式不正确"),
    BALANCE_NOT_ENOUGH(-6,"手客户余额不足"),
    PARAMETER_ERROR(-10,"参数不合法");

    private Integer code;
    private String message;

    ExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
