package com.cai.library.mapper.admin;

import com.cai.library.dao.user.LoginUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface LoginMapper {
    int countUserNameAndPasswordByMap(Map map);
    int countAdminNameAndPasswordByMap(Map map);
    int countAdminByAccount(String account);

    int insertLoginUser(LoginUser loginUser);
    LoginUser selectLoginUserByMap (Map map);

    Integer countUserAccount(String useraccount);
}
