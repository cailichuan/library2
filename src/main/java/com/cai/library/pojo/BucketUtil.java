package com.cai.library.pojo;

import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.Objects;

public class BucketUtil {
    //令牌桶的默认容量
    static final int DEFAULT_MAX_COUNT=10;
    //默认增长速率为1
    static final int DEFAULT_CREATE_RATE=1;
    //hashmap存放令牌桶
    public static HashMap<String,BucketUtil> buckets=new HashMap<String,BucketUtil>();

//    自定义容量
    final int maxCount;
    //自定义增长速率1s几个令牌
    int createRate;
    //当前令牌数量
    int szie=0;

//    无参构造，默认令牌容量与增长速率
    public BucketUtil(){
       this(DEFAULT_MAX_COUNT,DEFAULT_CREATE_RATE);
    }


    //有参构造，自定义令牌容量与增长速率
    public BucketUtil(int maxCount, int createRate){
        this.maxCount=maxCount;
        this.createRate=createRate;
    }

    //判断令牌桶是否已经满
    public boolean isFull(){
        return szie==maxCount;
    }

    //根据速率自增生成一个令牌
    @Async
    public synchronized void incrTokens(){

        for (int i=0;i<createRate;i++){
            if (isFull()){
                return;
            }
            szie++;
        }
    }

    //取令牌
    public synchronized boolean getToken(){
        if (szie>0)
            szie--;
        else
            return false;
        return true;
    }


    //重写hashcode,相等的类必然有相同的hash值
    @Override
    public int hashCode() {
        return Objects.hash(maxCount,szie,createRate);
    }

    @Override
    public boolean equals(Object obj) {
       if (obj==null){
           return false;
       }
       BucketUtil bucketUtil=(BucketUtil) obj;
       if (bucketUtil.szie!=szie||
       bucketUtil.createRate!=createRate||
               bucketUtil.maxCount!=maxCount
       )
           return false;
       return true;
    }
}
