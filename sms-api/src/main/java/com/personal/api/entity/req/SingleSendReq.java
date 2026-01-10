package com.personal.api.entity.req;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SingleSendReq
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description
 */
@Data
public class SingleSendReq {
    /** 客户的apikey */
    @NotBlank(message = "apikey不能为空")
    private String apikey;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    /** 短信内容 */
    @NotBlank(message = "短信内容不能为空")
    private String text;

    /** 客户业务内的uid */
    private String uid;

    /** 0-验证码短信 1-通知类短信 2-营销类短信 */
    @Range(min = 0,max = 2,message = "只能输入0-2的整数")
    @NotNull
    private int state;
}
