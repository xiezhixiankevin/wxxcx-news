package com.example.demo.common;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //token失效异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = TokenException.class)
    public R handler(TokenException e){
        return R.failed( e.getMessage());
    }

}