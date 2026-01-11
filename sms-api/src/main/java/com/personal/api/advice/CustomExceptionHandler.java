package com.personal.api.advice;

import com.personal.common.exception.ApiException;
import com.personal.common.res.R;
import com.personal.common.res.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName ExceptionHandler
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description 异常处理器
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResultVO handleApiException(ApiException e) {
        return R.error(e.getCode(),e.getMessage());
    }
}
