package com.cai.library.service;

import com.cai.library.dao.user.LoginUser;
import com.cai.library.mapper.admin.LoginMapper;
import com.cai.library.pojo.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    LoginMapper loginMapper;
    @Autowired
    MD5 md5;
    //管理员登录
    public boolean adminLogin(String account,String password) throws Exception {
        Map<String,String> map=new HashMap();
        map.put("account",account);
        map.put("password",md5.md5(password,account));
        return loginMapper.countAdminNameAndPasswordByMap(map)==1;
    }

    //用户登录
    public LoginUser userLogin(String account, String password) throws Exception {
        Map<String,String> map=new HashMap();
        map.put("account",account);
        map.put("password",md5.md5(password,account));
        return loginMapper.selectLoginUserByMap(map);
    }

}
