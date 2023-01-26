package com.cai.library.mapper.admin;

import com.cai.library.dao.admin.BorrowOfBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BorrowOfBookMapper {

    int insertBorrow(BorrowOfBook borrow);

    int updateBorrowByMap(Map map);
    Integer selectLendById(String id);
    int selectStockById(String id);

    List<BorrowOfBook> selectBorrowByPage(Map offSetAndRowsMap);

    int getDataNumber();

    BorrowOfBook selectBorrowById(String id);

    int deleteBorrowById(String id);

    int countSelectBorrowAny(Map selectTxt);

    List<BorrowOfBook> selectBorrowByAny(Map selectTxt);

    int countBorrowById(String id);
}
