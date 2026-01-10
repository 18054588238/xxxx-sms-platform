package com.personal.api.entity.res;

/**
 * @ClassName R
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description
 */
public class R {
    public static ResultVO ok() {
        ResultVO r = new ResultVO();
        r.setCode(0);
        r.setMsg("接收成功");
        return r;
    }

    public static ResultVO error(Integer code,String message) {
        ResultVO r = new ResultVO();
        r.setCode(code);
        r.setMsg(message);
        return r;
    }
}
