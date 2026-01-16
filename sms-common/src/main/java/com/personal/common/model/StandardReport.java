package com.personal.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName StandardReport
 * @Author liupanpan
 * @Date 2026/1/16
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardReport implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 针对当前短信的唯一标识
     */
    private Long sequenceId;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 客户业务内的uid
     */
    private String uid;

    /**
     * 目标手机号
     */
    private String mobile;

    /**
     * 短信的发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 短信的发送状态， 0-等待ing，1-成功，2-失败
     */
    private int reportState;

    // 短信发送失败的原因
    private String errorMsg;

    // 是否需要发送状态报告
    private Integer isCallback;
    // 回调地址 - 发送状态报告地址
    private String callbackUrl;
}
