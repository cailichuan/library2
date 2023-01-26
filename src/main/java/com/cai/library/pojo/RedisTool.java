package com.cai.library.pojo;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class RedisTool {
    @Autowired
    private RedisTemplate redisTemplate;


    //获取有序集合元素
    public Double  zSocre(String zSetKey,String value){

       return redisTemplate.opsForZSet().score(zSetKey, value);

    }

    //有序集合score+
    public boolean zincrementScore(String zSetKey,String value,Double score){
        try {
            redisTemplate.opsForZSet().incrementScore(zSetKey,value,score);
            return true;
        }catch (Exception e){
            return false;
        }
    }




    //有序集合添加元素
    public boolean zadd(String zSetKey,String value ,Double score){

        if(score.compareTo(0.0)==0){
            throw new RuntimeException("socre不能为0");
        }
        try {


            redisTemplate.opsForZSet().add(zSetKey,value,score);
            return true;

        }catch (Exception e){
            return false;
        }

    }

    //有序集合按照score倒序排序
    public Set<String> zReverseRange(String zSetKey,int start,int end){

            return redisTemplate.opsForZSet().reverseRange(zSetKey,start,end);

    }

    //有序集合按照score顺序排序
        public Set<String> zRange(String zSetKey, int start, int end){


                return redisTemplate.opsForZSet().range(zSetKey,start,end);

        }





}
