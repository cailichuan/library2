package com.cai.library.controller;

import com.cai.library.exception.CurrentLimitingException;
import com.cai.library.exception.SendVerificationCodeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebExceptionController {

    //限流异常处理
    @ResponseBody
    @ExceptionHandler(CurrentLimitingException.class)
    public String CurrentLimitingException(CurrentLimitingException e){
        return "您被限流了";
    }

    //验证码发送异常处理
    @ResponseBody
    @ExceptionHandler(SendVerificationCodeException.class)
    public String SendVerificationCodeException(){
        return "邮件发送异常";
    }
}
