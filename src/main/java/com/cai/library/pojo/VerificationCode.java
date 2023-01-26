package com.cai.library.pojo;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class VerificationCode {

    //生成验证码
    public  String createverificationCode(){
        Random random = new Random();
        int verificationCode = random.nextInt(999999 - 100000 + 1) + 100000;
        return String.valueOf(verificationCode);
    }




    //删除验证码,防止出现读写冲突问题，给此方法设置了一个aop
    public  void clearVerificationCode(String mailPath){

        //该邮箱的信息是否存在，存在就删除
        if (MailRequest.mailTimeMap.containsKey(mailPath) &&
                MailRequest.verificationCodeMap.containsKey(mailPath)) {

            MailRequest.mailTimeMap.remove(mailPath);
            MailRequest.verificationCodeMap.remove(mailPath);

        }

    }

    //获取当前时间戳与验证码时间差，防止出现读写冲突问题，给此方法设置了一个aop
    public Long  getVerificationCodeOvertime(String mailPath,String vCode){

        //该邮箱的信息是否存在，存在就获取差
       if (MailRequest.mailTimeMap.containsKey(mailPath) &&
               MailRequest.verificationCodeMap.containsKey(mailPath)){
            return System.currentTimeMillis()-MailRequest.mailTimeMap.get(mailPath);
       }

       //不存在就返回-1

        return -1L;
    }



    //验证码验证
    public boolean verificationCodeverification(String verificationCode,String mailPath){
        //没有该邮箱的信息就false
        if (!(MailRequest.mailTimeMap.containsKey(mailPath) &&
                MailRequest.verificationCodeMap.containsKey(mailPath))){
            return false;
        }
        //验证验证码是否相等
        if (!MailRequest.verificationCodeMap.get(mailPath).equals(verificationCode)){
            return false;
        }

        //验证验证码是否超时，超时就删除返回flase
        Long currentTimeMillis = MailRequest.mailTimeMap.get(mailPath);
        if (System.currentTimeMillis()-currentTimeMillis>3600000) {
            clearVerificationCode(mailPath);
            return false;
        }

        return true;


    }
}
