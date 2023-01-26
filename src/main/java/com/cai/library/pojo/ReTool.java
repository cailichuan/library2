package com.cai.library.pojo;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//正则匹配类
@Component
public class ReTool {
    //电话号码匹配
    public boolean phoneNumber(String phoneNumber){
        String regex="1(\\d){10}";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();


    }
}
