package com.cai.library.exception;

public class CurrentLimitingException extends Exception{
    String message;
    public CurrentLimitingException(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
