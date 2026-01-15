package com.personal.common.exception;

import com.personal.common.enums.ExceptionEnums;
import lombok.Getter;

/**
 * @ClassName StrategyException
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description
 */
@Getter
public class StrategyException extends RuntimeException {
    private Integer code;

    public StrategyException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public StrategyException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums.getMessage());
        this.code = exceptionEnums.getCode();
    }


}
