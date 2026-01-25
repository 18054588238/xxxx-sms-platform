package com.personal.monitor.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/**
 * @ClassName MailUtil
 * @Author liupanpan
 * @Date 2026/1/25
 * @Description
 */

@Component
@RefreshScope
public class MailUtil {

    @Value("${spring.mail.username}")
    private String sendMail;

    @Value("${spring.mail.receiveMail}")
    private String confRecMail;

    @Resource
    private JavaMailSender javaMailSender;

    public String sendSimpleMsg(String msg, String receiveMail,String subject) {

        if (StringUtils.isEmpty(msg))  {
            return "请输入消息内容";
        } else if (StringUtils.isEmpty(receiveMail)){
            // 为空，则使用配置文件中的收件人邮箱
            receiveMail = confRecMail;
        } else if (!receiveMail.contains("@")) {
            return "请输入正确的邮箱";
        }

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(sendMail);
            mimeMessageHelper.setTo(receiveMail.split(",")); // 收件人支持多个
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(msg,true); // 支持html
            javaMailSender.send(mimeMessage);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
    }
}
