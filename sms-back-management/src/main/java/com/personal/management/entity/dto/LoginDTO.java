package com.personal.management.entity.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName LoginDTO
 * @Author liupanpan
 * @Date 2026/1/27
 * @Description
 */
@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    String username;
    @NotBlank(message = "密码不能为空")
    String password;
    @NotBlank(message = "验证码不能为空")
    String captcha;
    boolean rememberMe;
}
