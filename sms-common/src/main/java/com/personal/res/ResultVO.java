package com.personal.res;

import lombok.Data;

/**
 * @ClassName ResultVO
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description
 */
@Data
public class ResultVO {

    private Integer code;

    private String msg;

    private Integer count;

    private Long fee;

    private String uid;

    private String sid;

}
