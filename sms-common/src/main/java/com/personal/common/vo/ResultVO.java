package com.personal.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @ClassName ResultVO
 * @Author liupanpan
 * @Date 2026/1/27
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {
    private Integer code;
    private String msg;
}
