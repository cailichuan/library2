package com.cai.library.exception;
//发送邮件抛出异常
public class SendVerificationCodeException extends RuntimeException{
    private String message;
    public SendVerificationCodeException(String message) {
        this.message=message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
