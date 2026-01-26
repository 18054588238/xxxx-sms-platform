package com.personal.monitor.jobhandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @ClassName XxlJobHandler
 * @Author liupanpan
 * @Date 2026/1/25
 * @Description 监控客户余额
 */
@Component
public class ClientBalanceHandler {

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Autowired
    private MailUtil mailUtil;
    /*
    * %s 用于字符串（string）
    * %d 用于十进制整数（decimal integer）
    * %f 用于浮点数（float）
    * */
    String text = "客户您好，你在【烽火短信平台】内的余额仅剩余<h1>%.2f</h1>元，请您及时补充金额，避免影响你您的短信发送！";

    @XxlJob("clientBalanceHandler")
    public void monitor() {
        // 获取key
        Set<String> keys = cacheFeignClient.getScanKeys(CacheConstant.CLIENT_BALANCE+"*");

        // 获取该客户的账户余额（单位：厘）以及客户邮箱信息
        for (String key : keys) {

            // 获取该客户的账户余额（单位：厘）以及客户邮箱信息
            List<Object> values = cacheFeignClient.hMultiGet(key);

            // 判断是否余额不足
            if (msgCount < SmsConstant.BALANCE_LIMIT) {
                // 余额不足，发邮件告知
                mailUtil.sendSimpleMsg(String.format(text,),,"【烽火短信平台】提醒您余额不足。");
                log.info("客户余额不充足，客户：{},余额为：{}。",,);
                return;
            }
        }
    }
}
