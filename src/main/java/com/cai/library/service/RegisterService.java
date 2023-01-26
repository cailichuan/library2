package com.cai.library.service;

import com.cai.library.dao.admin.User;
import com.cai.library.dao.user.LoginUser;
import com.cai.library.exception.MYSqlException;
import com.cai.library.mapper.admin.LoginMapper;
import com.cai.library.mapper.admin.UserMapper;
import com.cai.library.pojo.GetSnowId;
import com.cai.library.pojo.MD5;
import com.cai.library.type.SnowIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    MD5 md5;

    @Autowired
    GetSnowId getSnowId;


    @Transactional
    public void addRegister(User user, LoginUser loginUser) throws Exception {


        //创建用户id
        String U_id = String.valueOf(getSnowId.setSonwIdType(SnowIdType.USER).nextId());
        user.setId(U_id);
        loginUser.setUserId(U_id);
        String md5pass = md5.md5(loginUser.getPassword(), loginUser.getAccount());
        loginUser.setPassword(md5pass);

        System.out.println(user);
        System.out.println(loginUser);

        //向数据库插入信息
//        Integer userInsert=userMapper.insertUser(user);
//        Integer loginUserInsert=loginMapper.insertLoginUser(loginUser);
//        System.out.println("user" + userInsert);
//        System.out.println("loginuser" + loginUserInsert);
//        if (userInsert==null || userInsert<1 || loginUserInsert==null || loginUserInsert<1){
//            throw new MYSqlException("创建注册用户的mysql执行失败");
//
//
//        }

        try {
            userMapper.insertUser(user);
            loginMapper.insertLoginUser(loginUser);
        }catch (Exception e){

            throw new MYSqlException(e.getMessage());
        }





    }
}
