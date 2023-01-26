package com.cai.library.pojo;

import com.cai.library.type.SnowIdType;
import org.springframework.stereotype.Component;


@Component
public class GetSnowId {
    //序列号位数
    private static final long serialNumberBit=12L;
    //服务ID位数
    private static final long workIdBit=5L;
    //机器ID位数
    private static final long machineIdBit=5L;
    //时间戳位数
    private static final long currentTimeMillisBit=41L;
    //最大的序列号
    private static final long maxSerialNumber=~(-1L<<serialNumberBit);
    //最大服务ID
    private static final long maxWorkId=~(-1L<<workIdBit);
    //最大的机器ID
    private static final long maxMachineId=~(-1L<<machineIdBit);
    //最大的时间戳
    private static final long maxcurrentTimeMillis=~(-1L<<currentTimeMillisBit);
    //序列号
    private long serialNumber=0;
    //服务ID
    private long workId;
    //机器ID
    private long machineId;
    //上一秒时间戳
   private static long lastCurrentTimeMillis=1672382200988L;


    //无参构造
    public GetSnowId(){
        setSonwIdType(SnowIdType.BOOK);
    }


        //设置机器ID和服务ID

    public GetSnowId setSonwIdType(SnowIdType snowIdType){
        Long workId=snowIdType.getWorkId();
        Long machineId=snowIdType.getMachineId();
        if (workId<0||workId>maxWorkId){
            throw new IllegalArgumentException("服务id不能小于0或者大于"+maxWorkId);

        }
        if (machineId<0||machineId>maxMachineId){
            throw new IllegalArgumentException("机器id不能小于0或者大于"+maxMachineId);

        }
        this.workId=workId;
        this.machineId=machineId;

        return this;
    }
    //获取下一个时间戳
    public long getNextCurrentTimeMillis(long lastCurrentTimeMillis){
        long thisCurrentTimeMillis=System.currentTimeMillis();
        while (thisCurrentTimeMillis<=lastCurrentTimeMillis){
            thisCurrentTimeMillis=System.currentTimeMillis();
        }
        return thisCurrentTimeMillis;
    }

    //锁方法
    public synchronized long nextId(){
        //获取当前时间戳
        long currentTimeMillis=System.currentTimeMillis();
        //判断时间戳是否与上一时间戳相等
        if(currentTimeMillis==lastCurrentTimeMillis){

            if (serialNumber==-1){
                serialNumber++;
            }

            else {
                //如果序列号已经最大那就获取下一个时间戳
                if ((++serialNumber&maxSerialNumber)==0) {
                    currentTimeMillis=getNextCurrentTimeMillis(currentTimeMillis);
                }

            }

        }

        //不等于上一时间戳
        else {
            serialNumber=0;
        }

        //更新上一时间戳信息
        lastCurrentTimeMillis=currentTimeMillis;

        //拼接雪花ID
        return currentTimeMillis<<(machineIdBit+workIdBit+serialNumberBit)|
                machineId<<(workIdBit+serialNumberBit)|
                workId<<(serialNumberBit)|
                serialNumber;





    }

}
