package com.cai.library.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailRequest implements Serializable {
    //注入生成6位验证码的类
    @Autowired
    VerificationCode verificationCode;
    //注入邮件工具类
    @Autowired
    private JavaMailSender javaMailSender;
 /*
    发件人
    */
    private String sendMailer;
/*
    接收人
    */
    private String sendTo;
 /*
    邮件主题
    */
    private String subject;
 /*
    邮件内容
*/
    private String text;
/*
    附件路径
    */
    private String filePath;
/*
    邮箱发送时间
*/
    public static Map<String,Long>mailTimeMap=new HashMap<String,Long>();
/*
    邮箱验证码
*/
    public static Map<String,String>verificationCodeMap=new HashMap<String,String>();

    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String getSendMailer() {
        return sendMailer;
    }

    public void setSendMailer(String sendMailer) {
        this.sendMailer = sendMailer;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {

        this.sendTo = sendTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "MailRequest{" +
                "sendMailer='" + sendMailer + '\'' +
                ", sendTo=" +sendTo+
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    /*
    发送验证码
    */

    public boolean  sendVerificationCode(){
        System.out.println(this);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sendMailer);
            message.setTo(sendTo);
            message.setSubject("验证码");
            //生成验证码

            message.setText(text);

            javaMailSender.send(message);


            //记录发送时间和验证码
            MailRequest.mailTimeMap.put(sendTo,System.currentTimeMillis());
            MailRequest.verificationCodeMap.put(sendTo,text);
            return true;
        }catch (Exception e){
            return false;


        }


    }
}
