package com.personal.strategy.utils;

import com.alibaba.fastjson.JSON;
import com.personal.strategy.model.res.MobileOperatorRes;
import com.personal.strategy.model.vo.MobileOperatorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @ClassName mobileOperatorUtil
 * @Author liupanpan
 * @Date 2026/1/13
 * @Description 调用第三方接口查询手机号的运营商信息
 */
@Component
public class MobileOperatorUtil {

    String url = "https://cx.shouji.360.cn/phonearea.php?number=";

    @Autowired
    private RestTemplate restTemplate;

    public String getMobileOperator(String phoneNumber) {
        String fullUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("number", phoneNumber) // 自动编码参数
                .build()
                .toString();
        String jsonRes = restTemplate.getForObject(fullUrl, String.class);

        MobileOperatorRes mobileOperatorVo = JSON.parseObject(jsonRes, MobileOperatorRes.class);
        if (mobileOperatorVo != null && mobileOperatorVo.getCode() == 0) {
            MobileOperatorVo data = mobileOperatorVo.getData();
            String province = data.getProvince();
            String city = data.getCity();
            String sp = data.getSp();
            return province +" "+ city +","+ sp;
        }
        return null;
    }


}
