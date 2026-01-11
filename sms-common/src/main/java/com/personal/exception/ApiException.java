package com.personal.exception;

import com.personal.enums.ExceptionEnums;
import lombok.Getter;

/**
 * @ClassName ApiException
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description
 */
@Getter
public class ApiException extends RuntimeException {
    private Integer code;

    public ApiException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public ApiException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums.getMessage());
        this.code = exceptionEnums.getCode();
    }


}
