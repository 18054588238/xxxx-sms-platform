package com.personal.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MobileArea
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileArea {

    private String mobileNumber;
    /**
     * 手机号区域
     */
    private String mobileArea;
    /**
     * 手机号运营商
     */
    private String mobileType;
}
