package com.learning.gulimall.common.exception;

import com.learning.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GulimallExceptionAdvice {

    @ExceptionHandler(value = WebExchangeBindException.class)
    public R handleException(WebExchangeBindException e) {
        log.error("验证失败：{}，异常类型：{}",e.getMessage(), e.getClass());
        Map<String, Object> errorMap = new HashMap<>();
        // 将错误信息放入map中
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", errorMap);
    }
}
