package com.cai.library.mapper.admin;

import com.cai.library.dao.admin.BorrowOfUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface BorrowOfUserMapper {
    int getDataNumber();

    int insertBorrow(BorrowOfUser borrow);




    int deleteBorrowByMap(Map map);

    int countBorrowByIdAndBorrwMap(Map idAndBorrowMap);

    int countSelectBorrowAny(Map selectTxt);

    int deleteBorrowByBookId(String bookId);

    String selectBookIdById(String id);


    int updateBorrowByMap(Map map);

    BorrowOfUser selectBorrowByMap(Map idAndBorrowMap);

    BorrowOfUser selectUserNameAndBookTitleById(String id);

    List<Integer> selectActNumberByIdAndBorrowMap(Map idAndBorrowMap);

    List<Date> selectDateByIdAndBorrowMap(Map idAndBorrowMap);

    List<BorrowOfUser> selectBorrowByAny(Map selectTxt);

    List<BorrowOfUser> selectByPage(Map offsetRowsMap);

    List<String> selectIdByMap(Map userIdAndBorrowMap);



}
