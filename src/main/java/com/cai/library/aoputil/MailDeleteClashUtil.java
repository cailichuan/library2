package com.cai.library.aoputil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Aspect
//删除邮箱的上锁aop，防止删除邮箱产生的读写冲突
public class MailDeleteClashUtil {
    public static Set<String> deletingMailSet=new HashSet<String>();


    //删除邮箱的方法
    @Pointcut("execution(public void com.cai.library.pojo.VerificationCode.clearVerificationCode(String))")
    public void deleteMail(){

    }

    //获取当前时间戳与验证码时间差方法
    @Pointcut("execution(public Long com.cai.library.pojo.VerificationCode.getVerificationCodeOvertime(String,String))")
    public void getOverTime(){

    }


    @Around("deleteMail()")
    public Object deleteMailClash(ProceedingJoinPoint pjp) throws Throwable {

        //获取要删除的参数
        Object[] args = pjp.getArgs();
        String mail=(String) args[0];

        //检查要删除的mail是否在deletingMailSet里，没有继续执行，有就while循环
        while (deletingMailSet.contains(mail));

        //开始删除操作之前在deletingMailSet里登记
        deletingMailSet.add(mail);

        Object proceed = pjp.proceed();

        //删除操作成功后删去deletingMailSet的记录
        deletingMailSet.remove(mail);


        return proceed;
    }


    @Around("getOverTime()")
    public Object getOverTimeClash(ProceedingJoinPoint pjp) throws Throwable {
        //获取要获取的mail参数
        Object[] args = pjp.getArgs();
        String mail=(String) args[0];

        //检查要删除的mail是否在deletingMailSet里，没有继续执行，有就while循环
        while (deletingMailSet.contains(mail));

        //开始删除操作之前在deletingMailSet里登记
        deletingMailSet.add(mail);

        Object proceed = pjp.proceed();

        //删除操作成功后删去deletingMailSet的记录
        deletingMailSet.remove(mail);

        return proceed;
    }
}
