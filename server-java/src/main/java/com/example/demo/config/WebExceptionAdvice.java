package com.example.demo.config;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public R handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        return R.failed("服务器异常");
    }
}
