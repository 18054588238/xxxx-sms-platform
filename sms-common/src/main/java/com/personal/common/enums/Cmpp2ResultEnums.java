package com.personal.common.enums;

import lombok.Getter;

/**
 * @ClassName Cmpp2ResultEnums
 * @Author liupanpan
 * @Date 2026/1/24
 * @Description
 */
@Getter
public enum Cmpp2ResultEnums {
    /* -------------------运营商第一次响应--------------------- */
    OK(0,"正确"),
    MESSAGE_BUILD_ERROR(1, "消息结构错"),
    COMMAND_WORD_ERROR(2, "命令字错"),
    MESSAGE_SEQUENCE_ERROR(3, "消息序号重复"),
    MESSAGE_LENGTH_ERROR(4, "消息长度错"),
    INCORRECT_TARIFF_CODE(5, "资费代码错"),
    EXCEEDING_MAXIMUM_MESSAGE_LENGTH(6, "超过最大信息长度"),
    BUSINESS_CODE_ERROR(7, "业务代码错"),
    FLOW_CONTROL_ERROR(8, "流量控制错"),
    UNKNOWN(9, "其他错误")
    ;
    private Integer code;
    private String msg;

    Cmpp2ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(Integer code) {
        for (Cmpp2ResultEnums item : Cmpp2ResultEnums.values()) {
            if (item.getCode().equals(code)) {
                return item.msg;
            }
        }
        return null;
    }
}
