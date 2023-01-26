package com.cai.library.dataCreate.pojo;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateUuid {
    public String toUuid(){
        return UUID.randomUUID().toString();
    }
}
