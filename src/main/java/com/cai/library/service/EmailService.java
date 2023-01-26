package com.cai.library.service;

import com.cai.library.pojo.MailRequest;
import com.cai.library.exception.SendVerificationCodeException;
import com.cai.library.pojo.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    MailRequest mailRequest;
    @Autowired
    VerificationCode vCode;

    private static String sendFrom="848349974@qq.com";

    //发送验证码
    public void sendVerificationCode(String toMail){
        //设置发送人
        mailRequest.setSendMailer(sendFrom);
        //设置接收人
        mailRequest.setSendTo(toMail);
        //设置验证码
        String verificationCode=vCode.createverificationCode();
        mailRequest.setText(verificationCode);

        //发送
        if (!mailRequest.sendVerificationCode()){
            throw new SendVerificationCodeException("亲，邮件发送异常。");
        }




    }
}
