package com.cai.library.service.admin;

import com.cai.library.dao.admin.BorrowOfBook;
import com.cai.library.dao.admin.BorrowOfUser;
import com.cai.library.dao.admin.PageInput;
import com.cai.library.dataCreate.pojo.CreateUuid;
import com.cai.library.exception.ServiceException;
import com.cai.library.mapper.admin.BookMapper;
import com.cai.library.mapper.admin.BorrowOfBookMapper;
import com.cai.library.mapper.admin.BorrowOfUserMapper;
import com.cai.library.pojo.*;
import com.cai.library.type.SnowIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BorrowOfUserService {
    @Autowired
    BorrowOfUserMapper borrowOfUserMapper;
    @Autowired
    BorrowOfBookMapper borrowOfBookMapper;
    @Autowired
    BookMapper bookMapper;

    @Autowired
    GetSnowId getSnowId;
    @Autowired
    GetPageInputOrMaxPage getPageInputOrMaxPage;
    @Autowired
    GetPageOffsetRows getPageOffsetRows;
    @Autowired
    CreateUuid createUuid;

    @Autowired
    DataTool dataTool;


    @Autowired
    VerificationTool verificationTool;

    public Map<String,Object> selectBorrowByPage(int page){
        Map<String,Object> thisPageDataAndPageInputMap=new HashMap<>();
        //判断传入的页数是否合法或者搜索结果是否为空
        int dataNumber= borrowOfUserMapper.getDataNumber();
        int maxPage=getPageInputOrMaxPage.getMaxPage(dataNumber);
        if(page<1 || page>maxPage || dataNumber==0){
            return null;
        }


        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(page);

        //该页数据和翻页组件的获取
        thisPageDataAndPageInputMap.put("thisPageData",borrowOfUserMapper.selectByPage(pageOffsetRowsMap));
        thisPageDataAndPageInputMap.put("PageInput",getPageInputOrMaxPage.getPageInput(page,dataNumber));

        return thisPageDataAndPageInputMap ;
    }

    //获取修改数据
    public BorrowOfUser getEditInfo(Map idAndBorrowMap) {
        return borrowOfUserMapper.selectBorrowByMap(idAndBorrowMap);
    }



    //修改数据
    @Transactional
    public void upDateBorrow(BorrowOfUser borrow,int increment) {

        try {

            switch (borrow.getBorrow()){

                case 0:
                {

                    //获取书籍总数
                    int gross=bookMapper.selectNumberById(borrow.getBookId());

                    //获取书籍借出信息
                    int lend=borrowOfBookMapper.selectLendById(borrow.getBookId())-increment;

                    //获取书籍库存
                    int stock = gross - lend;

                    //如果借出为0则删除该图书id的书籍借阅信息
                    if (lend == 0){
                        borrowOfBookMapper.deleteBorrowById(borrow.getBookId());
                    } else {
                        //否则修正图书id的图书借阅信息
                        Map<String,Object> map = new HashMap<>();
                        map.put("id",borrow.getBookId());
                        map.put("lend",lend);
                        map.put("stock",stock);
                        borrowOfBookMapper.updateBorrowByMap(map);
                    }

                    Map<String,Object> updateBorrowis0Map=new HashMap();
                    updateBorrowis0Map.put("id",borrow.getId());
                    updateBorrowis0Map.put("date",borrow.getDate());
                    updateBorrowis0Map.put("actNumber",borrow.getActNumber());
                    updateBorrowis0Map.put("originalDate",borrow.getOriginalDate());
                    updateBorrowis0Map.put("borrow",0);
                    borrowOfUserMapper.updateBorrowByMap(updateBorrowis0Map);

                }


                //修改borow==1的数据，同时修改同一id下borrow=0的bookid和userid
                case 1:
                {

                    //搜索未修改前的图书id
                    String originalBookId = borrowOfUserMapper.selectBookIdById(borrow.getId());
                    //否修改了图书id
                    System.out.println("原来图书id"+originalBookId);
                    System.out.println("现图书id" + borrow.getBookId());
                    if(!originalBookId.equals(borrow.getBookId())){

//                        原图书id书籍借阅信息的处理
                        int originalLend = borrowOfBookMapper.selectLendById(originalBookId)
                                -borrow.getActNumber()-dataTool.getThisIdBorrowOfUserLend(borrow.getId());
                        //如果原图书id的借阅信息的lend=0，则删除原图书id的借阅信息
                        if(originalLend<=0){
                            borrowOfBookMapper.deleteBorrowById(originalBookId);


                            //否则则修正员图书id信息
                        } else {
                            int originalGross = borrowOfBookMapper.selectStockById(originalBookId);
                            int originalStock = originalGross-originalLend;

                            Map<String,Object> map = new HashMap();
                            map.put("id",originalBookId);
                            map.put("stock",originalStock);
                            map.put("lend",originalLend);
                            borrowOfBookMapper.updateBorrowByMap(map);
                        }

                        int gross = bookMapper.selectNumberById(borrow.getBookId());

                        //现图书id书籍信息不存在则新建一条信息
                        if(borrowOfBookMapper.countBorrowById(borrow.getBookId())<1){
                            BorrowOfBook borrowOfBook = new BorrowOfBook();
                            borrowOfBook.setId(borrow.getBookId());
                            borrowOfBook.setGross(gross);
                            borrowOfBook.setStock(gross-borrow.getActNumber());
                            borrowOfBook.setLend(borrow.getActNumber());
                            borrowOfBookMapper.insertBorrow(borrowOfBook);

                        }else {
                            //若存在，修正现图书id的借阅信息
                            int lend = borrowOfBookMapper.selectLendById(borrow.getBookId())+borrow.getActNumber();
                            int stock = gross - lend;
                            Map<String,Object> map = new HashMap();
                            map.put("id",borrow.getBookId());
                            map.put("lend",lend);
                            map.put("stock",stock);
                            borrowOfBookMapper.updateBorrowByMap(map);


                        }


                    }else {
                        //没有修改图书id
                        int gross = bookMapper.selectNumberById(borrow.getBookId());
                        System.out.println("增量=" + increment);
                        int lend = borrowOfBookMapper.selectLendById(borrow.getBookId())+increment;
                        int stock = gross-lend;
                        Map<String,Object> map = new HashMap();
                        map.put("id",borrow.getBookId());
                        map.put("lend",lend);
                        map.put("stock",stock);
                        borrowOfBookMapper.updateBorrowByMap(map);

                    }




                    //装配修改同一id下borrow=0的数据
                    Map<String,Object> userIdAndBookIdMap=new HashMap();
                    userIdAndBookIdMap.put("id",borrow.getId());
                    userIdAndBookIdMap.put("userId",borrow.getUserId());
                    userIdAndBookIdMap.put("bookId",borrow.getBookId());
                    userIdAndBookIdMap.put("borrow",0);

                    //装配修改borrow=1的数据
                    Map<String,Object> allMap=new HashMap<>();
                    allMap.put("id",borrow.getId());
                    allMap.put("userId",borrow.getUserId());
                    allMap.put("bookId",borrow.getBookId());
                    allMap.put("actNumber",borrow.getActNumber());
                    allMap.put("date",borrow.getDate());
                    allMap.put("borrow",1);



                    //修改同一id下borrow=0的数据
                    borrowOfUserMapper.updateBorrowByMap(userIdAndBookIdMap);

                    //修改borrow=1的数据
                    borrowOfUserMapper.updateBorrowByMap(allMap);

                }




            }






        }
        catch (Exception e){
            e.printStackTrace();
            throw new ServiceException();
        }







    }

    //添加数据
    @Transactional
    public void addBorrow(BorrowOfUser borrow) {

        try {
            switch (borrow.getBorrow()){

                //增加的为还书信息
                case 0:
                {
                    //获取图书总量
                    //图书总量
                    int gross= bookMapper.selectNumberById(borrow.getBookId());
                    //获取图书借还
                    int stock = borrowOfBookMapper.selectStockById(borrow.getBookId())+borrow.getActNumber();
                    //获取图书库存
                    int lend = gross-stock;



                    //如果借出为0则删除该图书id的书籍借阅信息
                    if (lend == 0){
                        borrowOfBookMapper.deleteBorrowById(borrow.getBookId());
                    } else {
                        //否则修正图书id的图书借阅信息
                        Map<String,Object> map = new HashMap<>();
                        map.put("id",borrow.getBookId());
                        map.put("lend",lend);
                        map.put("stock",stock);
                        borrowOfBookMapper.updateBorrowByMap(map);
                    }

                    borrowOfUserMapper.insertBorrow(borrow);
                }


                //增加的为借书信息
                case 1:
                {
                    //创建新的id
                    borrow.setId(String.valueOf(getSnowId.setSonwIdType(SnowIdType.B_AND_L).nextId()));
                    //图书总量
                    int gross= bookMapper.selectNumberById(borrow.getBookId());

                    //如果图书借阅信息不存在则添加图书借阅信息
                    if (!verificationTool.borrowOfBookVerification(borrow.getBookId())){

                        //借出
                        int lend=borrow.getActNumber();
                        //库存
                        int stock=gross-lend;

                        BorrowOfBook borrowOfBook=new BorrowOfBook();
                        borrowOfBook.setId(borrow.getBookId());
                        borrowOfBook.setGross(gross);
                        borrowOfBook.setLend(lend);
                        borrowOfBook.setStock(stock);


                        borrowOfBookMapper.insertBorrow(borrowOfBook);

                        //如果已经存在,则修正信息
                    }else {

                        //借出
                        int lend=borrowOfBookMapper.selectLendById(borrow.getBookId())+borrow.getActNumber();
                        //库存
                        int stock=gross-lend;
                        Map<String,Object> map=new HashMap<>();
                        map.put("id",borrow.getBookId());
                        map.put("lend",lend);
                        map.put("stock",stock);

                        borrowOfBookMapper.updateBorrowByMap(map);
                    }
                    borrowOfUserMapper.insertBorrow(borrow);


                }

            }
        }
        catch (Exception e){


            throw new ServiceException();

        }







    }



    //搜索框搜索数据
    public Map<String, Object> selectBorrowByAny(String selectTxt, int selectKindValue, int thisPage){

        //判断搜索条件
        String selectKey="noKindSelectText";
        switch (selectKindValue){
            case 1:
                selectKey="userName";
                break;
            case 2:
                selectKey="bookTitle";
                break;
        }

        //装配要查询的参数
        Map<String,Object> selectBorrowrByAnyMap=new HashMap<>();
        Map<String,Object> countSelectUserByAnyMap=new HashMap<>();
        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(thisPage);
        selectBorrowrByAnyMap.put(selectKey,selectTxt);
        selectBorrowrByAnyMap.put("offset",pageOffsetRowsMap.get("offset"));
        selectBorrowrByAnyMap.put("rows", pageOffsetRowsMap.get("rows"));

        countSelectUserByAnyMap.put(selectKey,selectTxt);

        //判断传入参数是否合理
        int dataNumber=borrowOfUserMapper.countSelectBorrowAny(countSelectUserByAnyMap);
        int maxPage=getPageInputOrMaxPage.getMaxPage(dataNumber);
        if (thisPage>maxPage){
            return null;
        }
        //装配翻页插件
        List<BorrowOfUser>borrows=borrowOfUserMapper.selectBorrowByAny(selectBorrowrByAnyMap);

        Map<String,Object> selectBorrowByAnyOrPageInputMap=new HashMap<>();


        PageInput pageInput = getPageInputOrMaxPage.getPageInput(thisPage, dataNumber);
        selectBorrowByAnyOrPageInputMap.put("sBorrows",borrows);


        selectBorrowByAnyOrPageInputMap.put("pageInput",pageInput);
        return selectBorrowByAnyOrPageInputMap;
    }

    //根据信息词条的id查询书名与用户名
    public Map selectUserNameAndBookTitleById(String id){

        BorrowOfUser borrow = borrowOfUserMapper.selectUserNameAndBookTitleById(id);

        Map<String,String> userNameAndBookTitleMap= new HashMap<>();
        userNameAndBookTitleMap.put("bookTitle",borrow.getBookTitle());
        userNameAndBookTitleMap.put("userName", borrow.getUserName());
        return userNameAndBookTitleMap;
    }

    //删除数据
    @Transactional
    public void deleteBorrow(BorrowOfUser borrow){
        try {
            switch (borrow.getBorrow()){


                case 0:
                {
                    //修正图书id的书籍借阅信息
                    int gross = bookMapper.selectNumberById(borrow.getBookId());

                    //本来lend=0的书因为删除还书信息，导致lend<0,需要重新添加
                    if (borrowOfBookMapper.countBorrowById(borrow.getBookId()) <1) {
                        int lend = borrow.getActNumber();
                        int stock = gross-lend;
                        BorrowOfBook borrowOfBook = new BorrowOfBook();
                        borrowOfBook.setId(borrow.getBookId());
                        borrowOfBook.setGross(gross);
                        borrowOfBook.setLend(lend);
                        borrowOfBook.setStock(stock);

                        borrowOfBookMapper.insertBorrow(borrowOfBook);
                    }else {
                        //若该图书id的书籍借阅信息还存在则修正信息
                        int lend = borrowOfBookMapper.selectLendById(borrow.getBookId()) + borrow.getActNumber();
                        int stock = gross-lend;
                        Map<String,Object> map=new HashMap();
                        map.put("id",borrow.getBookId());
                        map.put("lend",lend);
                        map.put("stock",stock);
                        borrowOfBookMapper.updateBorrowByMap(map);
                    }


                    Map<String,Object> idAndBorrowAndDateMap = new HashMap<>();
                    idAndBorrowAndDateMap.put("id",borrow.getId());
                    idAndBorrowAndDateMap.put("borrow",0);
                    idAndBorrowAndDateMap.put("date",borrow.getDate());
                    borrowOfUserMapper.deleteBorrowByMap(idAndBorrowAndDateMap);

                }



                case 1:
                {

                    int gross=bookMapper.selectNumberById(borrow.getBookId());

                    int lend=borrowOfBookMapper.selectLendById(borrow.getBookId())
                            -dataTool.getThisIdBorrowOfUserLend(borrow.getId());
                    //                如果该图书id的lend为0则直接删除该条书籍借阅信息
                    if (lend<=0){
                        borrowOfBookMapper.deleteBorrowById(borrow.getBookId());
                    } else {
                        //否则修正书籍借阅信息
                        int stock = gross-lend;
                        Map<String,Object> map = new HashMap();
                        map.put("id",borrow.getBookId());
                        map.put("stock",stock);
                        map.put("lend",lend);
                        borrowOfBookMapper.updateBorrowByMap(map);

                    }


                    Map<String,Object> idMap = new HashMap<>();
                    idMap.put("id",borrow.getId());
                    borrowOfUserMapper.deleteBorrowByMap(idMap);

                }


            }
        }catch (Exception e){


            throw new ServiceException();
        }






    }



}
