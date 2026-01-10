package com.personal.api.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @ClassName SmsCodeEnum
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 为了更好的管理code码以及对应的信息，声明一个枚举去管理
 */
@Getter
public enum SmsCodeEnum {
    PARAMETER_ERROR(-10,"参数不合法");

    private Integer code;

    private String msg;

    SmsCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
