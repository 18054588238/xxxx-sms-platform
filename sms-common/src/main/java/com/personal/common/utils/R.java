package com.personal.common.utils;

import com.personal.common.enums.ExceptionEnums;
import com.personal.common.vo.ResultVO;

import java.util.Objects;

/**
 * @ClassName R
 * @Author liupanpan
 * @Date 2026/1/27
 * @Description
 */
public class R {

    public static ResultVO ok(String msg) {
        return new ResultVO(0,msg);
    }

    public static ResultVO ok(String msg,Object data) {
        return new ResultVO(0,msg,data);
    }

    public static ResultVO error(ExceptionEnums enums) {
        return new ResultVO(enums.getCode(),enums.getMessage());
    }
}
