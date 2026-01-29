package com.personal.management.controller;

import com.personal.common.constants.BackManageConstant;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.utils.R;
import com.personal.common.vo.ResultVO;
import com.personal.management.entity.SmsUser;
import com.personal.management.entity.dto.LoginDTO;
import com.personal.management.service.SmsMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserController
 * @Author liupanpan
 * @Date 2026/1/27
 * @Description
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class UserController {

    @Autowired
    private SmsMenuService menuService;

    @PostMapping("/login")
    public ResultVO login(@RequestBody @Valid LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("【认证操作】参数不合法，loginDTO={},e={}", loginDTO,bindingResult.getAllErrors());
            return R.error(ExceptionEnums.PARAMETER_ERROR);
        }
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        String kaptcha = loginDTO.getCaptcha();

        // 判断验证码是否正确
        String kap = (String) SecurityUtils.getSubject().getSession().getAttribute(BackManageConstant.KAPTCHA);
        if (!kaptcha.equals(kap)) {
            // 验证码不正确
            log.info("验证码不正确,正确的验证码：{},你输入的验证码：{}", kap, kaptcha);
            return R.error(ExceptionEnums.KAPACHA_ERROR);
        }
        // 判断是否通过认证
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password, loginDTO.isRememberMe());
        try {
            SecurityUtils.getSubject().login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            log.info("用户名或密码错误，exception：{}", e.getMessage());
            return R.error(ExceptionEnums.AUTHEN_ERROR);
        }
        return R.ok("登录成功");
    }

    @GetMapping("/user/info")
    public ResultVO getUserInfo() {
        // 获取登录用户信息
        SmsUser smsUser = getSmsUser();

        if (smsUser == null) {
            log.info("【获取登录用户信息】 用户未登录！！");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("nickname", smsUser.getNickname());
        data.put("username", smsUser.getUsername());
        return R.ok(data);
    }

    private static SmsUser getSmsUser() {
        Subject subject = SecurityUtils.getSubject();
        SmsUser smsUser = (SmsUser) subject.getPrincipal();
        return smsUser;
    }

    // 获取当前登录用户权限下的菜单列表
    @GetMapping("/menu/user")
    public ResultVO getMenuUser() {
        SmsUser smsUser = getSmsUser();

        if (smsUser == null) {
            log.info("【获取登录用户信息】 用户未登录！！");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }
        // 树形结构
        List<Map<String,Object>> data = menuService.getMenuListByUserId(smsUser.getId());

        return R.ok(data);
    }
}
