package com.cai.library.service.admin;

import com.cai.library.dao.admin.BorrowOfBook;
import com.cai.library.dao.admin.PageInput;
import com.cai.library.mapper.admin.BookMapper;
import com.cai.library.mapper.admin.BorrowOfBookMapper;
import com.cai.library.pojo.GetPageInputOrMaxPage;
import com.cai.library.pojo.GetPageOffsetRows;
import com.cai.library.pojo.VerificationTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BorrowOfBookService {

    @Autowired
    BorrowOfBookMapper borrowOfBookMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    BorrowOfBookService borrowOfBookService;
    @Autowired
    GetPageInputOrMaxPage getPageInputOrMaxPage;
    @Autowired
    GetPageOffsetRows getPageOffsetRows;

    @Autowired
    VerificationTool verificationTool;


    public Map<String,Object> selectBorrowByPage(int page){
        Map<String,Object> thisPageDataAndPageInputMap=new HashMap<>();
        //判断传入的页数是否合法或者搜索结果是否为空
        int dataNumber= borrowOfBookMapper.getDataNumber();
        int maxPage=getPageInputOrMaxPage.getMaxPage(dataNumber);
        if(page<1 || page>maxPage || dataNumber==0){
            return null;
        }


        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(page);

        //该页数据和翻页组件的获取
        thisPageDataAndPageInputMap.put("thisPageData",borrowOfBookMapper.selectBorrowByPage(pageOffsetRowsMap));
        thisPageDataAndPageInputMap.put("PageInput",getPageInputOrMaxPage.getPageInput(page,dataNumber));

        return thisPageDataAndPageInputMap ;
    }


    public Map<String, Object> selectBorrowByAny(String selectTxt, int thisPage){



        //装配要查询的参数
        Map<String,Object> selectBorrowrByAnyMap=new HashMap<>();
        Map<String,Object> countSelectUserByAnyMap=new HashMap<>();
        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(thisPage);
        selectBorrowrByAnyMap.put("bookTitle",selectTxt);
        selectBorrowrByAnyMap.put("offset",pageOffsetRowsMap.get("offset"));
        selectBorrowrByAnyMap.put("rows", pageOffsetRowsMap.get("rows"));

        countSelectUserByAnyMap.put("bookTitle",selectTxt);

        //判断传入参数是否合理
        int dataNumber=borrowOfBookMapper.countSelectBorrowAny(countSelectUserByAnyMap);
        int maxPage=getPageInputOrMaxPage.getMaxPage(dataNumber);
        if (thisPage>maxPage){
            return null;
        }
        //装配翻页插件
        List<BorrowOfBook> borrows=borrowOfBookMapper.selectBorrowByAny(selectBorrowrByAnyMap);

        Map<String,Object> selectBorrowByAnyOrPageInputMap=new HashMap<>();


        PageInput pageInput = getPageInputOrMaxPage.getPageInput(thisPage, dataNumber);
        selectBorrowByAnyOrPageInputMap.put("sBorrows",borrows);


        selectBorrowByAnyOrPageInputMap.put("pageInput",pageInput);
        return selectBorrowByAnyOrPageInputMap;
    }


}

