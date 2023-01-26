package com.cai.library.pojo;


import com.cai.library.mapper.admin.BorrowOfUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class DataTool {
    @Autowired
    BorrowOfUserMapper borrowOfUserMapper;



    //获取同一id下borrw=0的最早时间
    public Date getBorrowIs0EarliestDate(String id){
        Map<String,Object> idAndBorrowMap=new HashMap<>();
        idAndBorrowMap.put("id",id);
        idAndBorrowMap.put("borrow",0);
        List<Date> dates = borrowOfUserMapper.selectDateByIdAndBorrowMap(idAndBorrowMap);
        Date earliestDate=dates.get(0);
        for (int i=1;i<dates.size();i++){
            if (dates.get(i).before(earliestDate)){
                earliestDate=dates.get(i);
            }
        }
        return earliestDate;
    }
    
    //获取该id的用户借书信息的当下实际借书数量
    public int getThisIdBorrowOfUserLend(String borrowOfUserId){
        //获取还书数量
        Map<String,Object> borrowIs0map = new HashMap();
        borrowIs0map.put("id",borrowOfUserId);
        borrowIs0map.put("borrow",0);
        List<Integer> yets = borrowOfUserMapper.selectActNumberByIdAndBorrowMap(borrowIs0map);
        int countYet = 0;
        for (Integer yet : yets) {
            countYet+=yet;
        }


        //获取借书数量
        Map<String,Object> borrowIs1map = new HashMap();
        borrowIs1map.put("id",borrowOfUserId);
        borrowIs1map.put("borrow",1);
        Integer lend = borrowOfUserMapper.selectActNumberByIdAndBorrowMap(borrowIs1map).get(0);

        //获取实际借书数量
        return lend-countYet;
    }
}
