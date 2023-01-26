package com.cai.library.mapper.admin;

import com.cai.library.dao.admin.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    int insertUser(User user);
    int updateUser(User user);
    List<User> selectUserByPage(Map offSetAndRowsMap);
    int getDataNumber();
    User selectUserById(String id);
    int deleteUserById(String id);
    int countSelectUserAny(Map selectTxt);
    List<User> selectUserByAny(Map selectTxt);
    int countSelectUserById(String id);
}
