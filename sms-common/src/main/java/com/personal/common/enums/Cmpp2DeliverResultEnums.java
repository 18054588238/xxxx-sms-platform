package com.personal.common.enums;

import lombok.Getter;

/**
 * @ClassName Cmpp2DeliverResultEnums
 * @Author liupanpan
 * @Date 2026/1/24
 * @Description
 */
@Getter
public enum Cmpp2DeliverResultEnums {
    /* -------------------运营商第二次响应--------------------- */
    DELIVERED("DELIVRD", "Message is delivered to destination"),
    EXPIRED("EXPIRED", "Message validity period has expired"),
    DELETED("DELETED", "Message has been deleted."),
    UNDELIVERABLE("UNDELIV", "Message is undeliverable"),
    ACCEPTED("ACCEPTD", "Message is in accepted state"),
    UNKNOWN("UNKNOWN", "Message is in invalid state"),
    REJECTED("REJECTD", "Message is in a rejected state"),
    ;
    private String code;
    private String msg;

    Cmpp2DeliverResultEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(String code) {
        for (Cmpp2DeliverResultEnums item : Cmpp2DeliverResultEnums.values()) {
            if (item.getCode().equals(code)) {
                return item.msg;
            }
        }
        return null;
    }
}
