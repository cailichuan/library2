package com.cai.library.exception;
//Service报错
public class ServiceException extends RuntimeException{
    private String message;
    public ServiceException(String message) {
        this.message=message;
    }
    public ServiceException(){

    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
