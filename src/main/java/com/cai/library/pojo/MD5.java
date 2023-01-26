package com.cai.library.pojo;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
//md5加密
public class MD5 {


    public static String md5(String text, String key) throws Exception {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text + key);
        return md5str;
    }

}

