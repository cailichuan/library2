package com.cai.library.service.user;

import com.cai.library.dao.admin.BorrowOfBook;
import com.cai.library.dao.admin.BorrowOfUser;
import com.cai.library.dao.admin.PageInput;
import com.cai.library.dao.user.LibraryBook;
import com.cai.library.dataCreate.pojo.CreateUuid;
import com.cai.library.exception.ServiceException;
import com.cai.library.mapper.admin.BookMapper;
import com.cai.library.mapper.admin.BorrowOfBookMapper;
import com.cai.library.mapper.admin.BorrowOfUserMapper;
import com.cai.library.mapper.user.UserLibraryMapper;
import com.cai.library.pojo.*;
import com.cai.library.type.SnowIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserLibraryService {
    @Autowired
    UserLibraryMapper userLibraryMapper;
    @Autowired
    GetPageInputOrMaxPage getPageInputOrMaxPage;
    @Autowired
    GetPageOffsetRows getPageOffsetRows;
    @Autowired
    CreateUuid createUuid;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    VerificationTool verificationTool;
    @Autowired
    BorrowOfBookMapper borrowOfBookMapper;
    @Autowired
    BorrowOfUserMapper borrowOfUserMapper;

    @Autowired
    HotBookRedisTool hotBookRedisTool;

    @Autowired
    GetSnowId getSnowId;


    public Map<String,Object> selectBookByPage(int page, int kindIndex){




        Map<String,Object> thisPageDataAndPageInputMap=new HashMap<>();
        System.out.println("index"+kindIndex);
        //判断传入的页数是否合法或者搜索结果是否为空
        int dataNumber= userLibraryMapper.getDataNumber(kindIndex-1);
        int maxPage=getPageInputOrMaxPage.getMaxPage(dataNumber);
        if(page<1 || page>maxPage || dataNumber==0){
            return null;
        }

//        //获取分类信息
//        List<Kind> kinds = kindMapper.selectAllKind();



        //获取数据的map
        Map<String,Object> map = new HashMap();
        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(page);
        //装配kindIndex条件
        if (kindIndex>=1){
            map.put("kindIndex",kindIndex-1);
        }
        map.put("offset",pageOffsetRowsMap.get("offset"));
        map.put("rows",pageOffsetRowsMap.get("rows"));



        //该页数据和翻页组件的获取
        thisPageDataAndPageInputMap.put("thisPageData",userLibraryMapper.selectLibraryBookByPage(map));
        thisPageDataAndPageInputMap.put("PageInput",getPageInputOrMaxPage.getPageInput(page,dataNumber));


        return thisPageDataAndPageInputMap ;
    }

    //搜索框搜索数据
    public Map selectBookByAny(int kindIndex,String selectTxt, int selectKindValue, int thisPage){

        //判断搜索条件
        String selectKey="noKindSelectText";
        switch (selectKindValue){
            case 1:
                selectKey="title";
                break;
            case 2:
                selectKey="author";
                break;
        }

        //装配要查询的参数
        Map<String,Object> selectBookByAnyMap=new HashMap<>();
        Map<String,Object> countSelectBookByAnyMap=new HashMap<>();
        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(thisPage);
        selectBookByAnyMap.put(selectKey,selectTxt);
        selectBookByAnyMap.put("offset",pageOffsetRowsMap.get("offset"));
        selectBookByAnyMap.put("rows", pageOffsetRowsMap.get("rows"));
        //装配kindIndex条件
        if (kindIndex>=1){
            selectBookByAnyMap.put("kindIndex",kindIndex-1);
            countSelectBookByAnyMap.put("kindIndex",kindIndex-1);
        }


        countSelectBookByAnyMap.put(selectKey,selectTxt);


        //判断传入参数是否合理
        int dataNumber=userLibraryMapper.countLibraryBookAny(countSelectBookByAnyMap);
        int maxPage=getPageInputOrMaxPage.getMaxPage(dataNumber);
        if (thisPage>maxPage){
            return null;
        }
        //装配翻页插件
        List<LibraryBook> libraryBooks=userLibraryMapper.selectLibraryBookByAny(selectBookByAnyMap);

        Map<String,Object> selectBookByAnyOrPageInputMap=new HashMap<>();


        PageInput pageInput = getPageInputOrMaxPage.getPageInput(thisPage, dataNumber);
        selectBookByAnyOrPageInputMap.put("libraryBooks",libraryBooks);
        selectBookByAnyOrPageInputMap.put("pageInput",pageInput);
        return selectBookByAnyOrPageInputMap;
    }

    //借书
    @Transactional
    public void addBorrow(LibraryBook libraryBook,String userId) {

            try {
                //图书总量
                int gross= bookMapper.selectNumberById(libraryBook.getId());

                //如果图书借阅信息不存在则添加图书借阅信息
                if (!verificationTool.borrowOfBookVerification(libraryBook.getId())){

                    //借出
                    int lend=libraryBook.getLend();
                    //库存
                    int stock=gross-lend;

                    BorrowOfBook borrowOfBook=new BorrowOfBook();
                    borrowOfBook.setId(libraryBook.getId());
                    borrowOfBook.setGross(gross);
                    borrowOfBook.setLend(lend);
                    borrowOfBook.setStock(stock);


                    borrowOfBookMapper.insertBorrow(borrowOfBook);

                    //如果已经存在,则修正信息
                }else {

                    //借出
                    int lend=borrowOfBookMapper.selectLendById(libraryBook.getId())+libraryBook.getLend();
                    //库存
                    int stock=gross-lend;
                    Map<String,Object> map=new HashMap<>();
                    map.put("id",libraryBook.getLend());
                    map.put("lend",lend);
                    map.put("stock",stock);

                    borrowOfBookMapper.updateBorrowByMap(map);
                }
                BorrowOfUser borrow = new BorrowOfUser();
                //创建新的id
                borrow.setId(String.valueOf(getSnowId.setSonwIdType(SnowIdType.B_AND_L).nextId()));
                borrow.setBookId(libraryBook.getId());
                borrow.setBorrow(1);
                borrow.setActNumber(libraryBook.getLend());
                borrow.setUserId(userId);

                //获取时间
                //SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                borrow.setDate(date);

                //热度处理
                hotBookRedisTool.countHot(libraryBook.getId());


                borrowOfUserMapper.insertBorrow(borrow);


            }
            catch (Exception e){
                System.out.println("报错="+e.getMessage());


                throw new ServiceException();
            }















    }

}
