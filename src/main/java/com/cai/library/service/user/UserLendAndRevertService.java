package com.cai.library.service.user;

import com.cai.library.dao.admin.BorrowOfBook;
import com.cai.library.dao.admin.BorrowOfUser;
import com.cai.library.dao.user.UserLendAndRevertBook;
import com.cai.library.exception.ServiceException;
import com.cai.library.mapper.admin.BookMapper;
import com.cai.library.mapper.admin.BorrowOfBookMapper;
import com.cai.library.mapper.admin.BorrowOfUserMapper;
import com.cai.library.mapper.admin.KindMapper;
import com.cai.library.mapper.user.UserLendAndRevertMapper;
import com.cai.library.pojo.GetPageInputOrMaxPage;
import com.cai.library.pojo.GetPageOffsetRows;
import com.cai.library.pojo.VerificationTool;
import com.cai.library.type.SnowIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserLendAndRevertService {

    @Autowired
    BookMapper bookMapper;
    @Autowired
    BorrowOfUserMapper borrowOfUserMapper;
    @Autowired
    BorrowOfBookMapper borrowOfBookMapper;

    @Autowired
    UserLendAndRevertMapper userLendAndRevertMapper;
    @Autowired
    KindMapper kindMapper;
    @Autowired
    GetPageOffsetRows getPageOffsetRows;
    @Autowired
    GetPageInputOrMaxPage getPageInputOrMaxPage;

    @Autowired
    VerificationTool verificationTool;

    //根据页数获取该页数据和翻页组件的
    //map:{该页数据，翻页组件}
    public Map<String, Object> selectBookByPage(int page, String userId) {


        Map<String, Object> thisPageDataAndPageInputMap = new HashMap<>();
        //判断传入的页数是否合法或者搜索结果是否为空
        int dataNumber = userLendAndRevertMapper.getDataNumber(userId);
        int maxPage = getPageInputOrMaxPage.getMaxPage(dataNumber);
        if (page < 1 || page > maxPage || dataNumber == 0) {
            return null;
        }


        //获取数据的map
        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(page);
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("userId", userId);
        dataMap.put("offset", pageOffsetRowsMap.get("offset"));
        dataMap.put("rows", pageOffsetRowsMap.get("rows"));

        //该页数据和翻页组件的获取
        List<UserLendAndRevertBook> userLendAndRevertBooks = userLendAndRevertMapper.selectByPage(dataMap);


        //获取实际借书数量
        for (UserLendAndRevertBook userLendAndRevertBook : userLendAndRevertBooks) {
            if (userLendAndRevertBook.getBorrowOrLend() == 1) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", userLendAndRevertBook.getId());
                map.put("borrow", 1);

                int actualBorrowingQuantity = borrowOfUserMapper.selectActNumberByIdAndBorrowMap(map).get(0) -
                        verificationTool.actNumberSumByIdWhenBorrwIS0(userLendAndRevertBook.getId());
                userLendAndRevertBook.setActualBorrowingQuantity(actualBorrowingQuantity);

            }
        }


        //返回该页数据和翻页组件
        thisPageDataAndPageInputMap.put("thisPageData", userLendAndRevertBooks);
        thisPageDataAndPageInputMap.put("PageInput", getPageInputOrMaxPage.getPageInput(page, dataNumber));
        return thisPageDataAndPageInputMap;
    }

    //还书
    @Transactional
    public void revertBook(UserLendAndRevertBook borrow, String userId) {
        try {
            //获取图书总量
            //图书总量
            int gross = bookMapper.selectNumberById(borrow.getBookId());
            //获取图书借还
            int stock = borrowOfBookMapper.selectStockById(borrow.getBookId()) + borrow.getActNumber();
            //获取图书库存
            int lend = gross - stock;


            //如果借出为0则删除该图书id的书籍借阅信息
            if (lend == 0) {
                borrowOfBookMapper.deleteBorrowById(borrow.getBookId());
            } else {
                //否则修正图书id的图书借阅信息
                Map<String, Object> map = new HashMap<>();
                map.put("id", borrow.getBookId());
                map.put("lend", lend);
                map.put("stock", stock);
                borrowOfBookMapper.updateBorrowByMap(map);
            }

            BorrowOfUser borrowOfUser = new BorrowOfUser();
            borrowOfUser.setId(borrow.getId());
            borrowOfUser.setBookId(borrow.getBookId());
            borrowOfUser.setUserId(userId);
            borrowOfUser.setBorrow(0);
            borrowOfUser.setActNumber(borrow.getActNumber());
            Date date = new Date(System.currentTimeMillis());
            borrowOfUser.setDate(date);


            borrowOfUserMapper.insertBorrow(borrowOfUser);

        } catch (Exception e) {

            throw new ServiceException();
        }


    }


    //获取实际借书数量
    public Integer getActualBorrowingQuantity(String borrowOfUserId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", borrowOfUserId);
        map.put("borrow", 1);


        return borrowOfUserMapper.selectActNumberByIdAndBorrowMap(map).get(0) -
                verificationTool.actNumberSumByIdWhenBorrwIS0(borrowOfUserId);

    }


    @Transactional
    public void addBorrow(BorrowOfUser borrow) {

        try {


            //增加的为还书信息


            //获取图书总量
            //图书总量
            int gross = bookMapper.selectNumberById(borrow.getBookId());
            //获取图书借还
            int stock = borrowOfBookMapper.selectStockById(borrow.getBookId()) + borrow.getActNumber();
            //获取图书库存
            int lend = gross - stock;


            //如果借出为0则删除该图书id的书籍借阅信息
            if (lend == 0) {
                borrowOfBookMapper.deleteBorrowById(borrow.getBookId());
            } else {
                //否则修正图书id的图书借阅信息
                Map<String, Object> map = new HashMap<>();
                map.put("id", borrow.getBookId());
                map.put("lend", lend);
                map.put("stock", stock);
                borrowOfBookMapper.updateBorrowByMap(map);
            }

            borrow.setDate(new Date(System.currentTimeMillis()));

            borrowOfUserMapper.insertBorrow(borrow);


        } catch (Exception e) {


            throw new ServiceException();

        }


    }


}
