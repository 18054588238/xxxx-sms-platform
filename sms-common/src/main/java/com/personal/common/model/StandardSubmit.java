package com.personal.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ClassName StandardSubmit
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 用于在各个模块中校验
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardSubmit {

    /**
     * 针对当前短信的唯一标识
     */
    private Long sequenceId;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 客户端的ip白名单
     */
    private String ip;

    /**
     * 客户业务内的uid
     */
    private String uid;

    /**
     * 目标手机号
     */
    private String mobile;

    /**
     * 短信内容的签名
     */
    private String sign;

    /**
     * 短信内容
     */
    private String text;

    /**
     * 短信的发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 当前短信的费用
     */
    private Long fee;

    /**
     * 目标手机号的运营商
     */
    private Integer operatorId;


    /**
     * 目标手机号的归属地区号  0451  0455
     */
    private Integer areaCode;

    /**
     * 目标手机号的归属地  哈尔滨，  绥化~
     */
    private String area;

    /**
     * 通道下发的源号码  106934985673485645
     */
    private String srcNumber;

    /**
     * 通道的id信息
     */
    private Long channelId;

    /**
     * 短信的发送状态， 0-等待ing，1-成功，2-失败
     */
    private int reportState;

    // 后续再做封装~~~~
    /*获取到的客户端真实IP地址*/
    private String realIP;
    /*客户端请求携带的apiKey*/
    private String apikey;
    /*0-验证码短信 1-通知类短信 2-营销类短信*/
    private int state;

    private Long signId;

}
