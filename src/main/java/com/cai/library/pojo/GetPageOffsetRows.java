package com.cai.library.pojo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetPageOffsetRows {
    public Map getPageOffsetRowsMap(int page){
        Map<String,Integer> pageOffsetRowsMap =new HashMap<String,Integer>();
        Integer offset = (page-1)*8;
        Integer rows=8;
        pageOffsetRowsMap.put("offset",offset);
        pageOffsetRowsMap.put("rows",rows);
        return pageOffsetRowsMap;


    }
}
