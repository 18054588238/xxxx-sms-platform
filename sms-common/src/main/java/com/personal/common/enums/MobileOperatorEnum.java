package com.personal.common.enums;

import lombok.Getter;

/**
 * @ClassName MobileOperatorEnum
 * @Author liupanpan
 * @Date 2026/1/13
 * @Description
 */
@Getter
public enum MobileOperatorEnum {
    CHINA_MOBILE(1,"移动"),
    CHINA_UNICOM(2,"联通"),
    CHINA_TELECOM(3,"电信"),
    CHINA_UNKNOWN(0,"未知");

    private Integer operateId;
    private String operateName;

    MobileOperatorEnum(Integer operateId, String operateName) {
        this.operateId = operateId;
        this.operateName = operateName;
    }
}
