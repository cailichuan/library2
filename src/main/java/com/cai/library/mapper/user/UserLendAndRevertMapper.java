package com.cai.library.mapper.user;

import com.cai.library.dao.user.UserLendAndRevertBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserLendAndRevertMapper {
    List<UserLendAndRevertBook> selectByPage(Map offsetRowsMap);

    int getDataNumber(String userId);
}
