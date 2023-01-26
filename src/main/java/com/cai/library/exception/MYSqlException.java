package com.cai.library.exception;

public class MYSqlException extends RuntimeException{
    String message;
    public MYSqlException(String message) {
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
