package com.cai.library;

import com.cai.library.anntation.CurrentLimiting;
import com.cai.library.pojo.BucketUtil;
import com.cai.library.exception.CurrentLimitingException;
import com.cai.library.pojo.MailRequest;
import com.cai.library.pojo.VerificationCode;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class Library2Application {
    @Autowired
    VerificationCode verificationCode;
    public static Set<String> bucketsNameSet=new HashSet<String>();
    public static int bucketSize=0;


    public static void main(String[] args) throws CurrentLimitingException {
        SpringApplication.run(Library2Application.class, args);
        //初始化令牌桶





        //扫描包名
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages("com.cai.library.controller")
                .addScanners(new MethodAnnotationsScanner()));

        //获取该包名下拥有CurrentLimiting注解的方法
        Set<Method> methods= reflections.getMethodsAnnotatedWith(CurrentLimiting.class);
        if (methods.size()>10){
            throw new CurrentLimitingException("CurrentLimiting注解使用超过默认十个");
        }

        //检查所有CurrentLimiting注解的bucketName是否重复，重复抛出异常否则初始化令牌桶
        for (Method method : methods) {
            CurrentLimiting annotation = method.getAnnotation(CurrentLimiting.class);
            if (!bucketsNameSet.contains(annotation.bucketName())) {
                bucketsNameSet.add(annotation.bucketName());
                BucketUtil bucket= new BucketUtil(annotation.maxCount(), annotation.createRate());
                BucketUtil.buckets.put(annotation.bucketName(),bucket);

            }else {
               throw new CurrentLimitingException("CurrentLimiting注解的bucketName="+annotation.bucketName()+"重复");
            }



        }


    }


    @Scheduled(fixedRate = 1000)//定时1s令牌桶每秒自动增长
    public void time1r(){

        for (String s : bucketsNameSet) {
            if (BucketUtil.buckets.containsKey(s))
                BucketUtil.buckets.get(s).incrTokens();
        }

    }


    //每隔一个小时就删除超过一个小时的验证码防止内存溢出
    @Scheduled(fixedRate = 3600000)
    void timer2(){
        //获取当前时间戳
        long thisCurrentTimeMillis = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> iterator = MailRequest.mailTimeMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Long> next = iterator.next();
            if (thisCurrentTimeMillis-next.getValue()>3600000) {
                verificationCode.clearVerificationCode(next.getKey());
            }
        }

    }

}
