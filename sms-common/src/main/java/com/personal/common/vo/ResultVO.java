package com.personal.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Object data;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Long total;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 为空就不响应给前端
    private Object rows;

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
