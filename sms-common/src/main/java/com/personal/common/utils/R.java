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

    /**
     * 成功，无数据
     * @return
     */
    public static ResultVO ok(){
        return new ResultVO(0,"");
    }

    /**
     * 成功，有数据
     * @return
     */
    public static ResultVO ok(Object data){
        ResultVO vo = ok();
        vo.setData(data);
        return vo;
    }

    /**
     * 成功，有数据
     * @return
     */
    public static ResultVO ok(Long total ,Object rows){
        ResultVO vo = ok();
        vo.setTotal(total);
        vo.setRows(rows);
        return vo;
    }

    public static ResultVO error(ExceptionEnums enums) {
        return new ResultVO(enums.getCode(),enums.getMessage());
    }
}
