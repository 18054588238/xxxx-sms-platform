package com.personal.strategy.model.res;

import com.personal.strategy.model.vo.MobileOperatorVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName MobileOperatorRes
 * @Author liupanpan
 * @Date 2026/1/13
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileOperatorRes implements Serializable {

    private int code;

    private MobileOperatorVo data;
}
