package com.personal.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName MobileOperatorVo
 * @Author liupanpan
 * @Date 2026/1/13
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileOperatorVo implements Serializable {

    private String province;

    private String city;

    private String sp;
}
