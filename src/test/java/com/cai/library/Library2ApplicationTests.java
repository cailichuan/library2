package com.cai.library;

import com.cai.library.dao.user.LibraryBook;
import com.cai.library.dataCreate.pojo.CreateUuid;
import com.cai.library.mapper.admin.BookMapper;
import com.cai.library.mapper.admin.BorrowOfUserMapper;
import com.cai.library.mapper.admin.UserMapper;
import com.cai.library.mapper.user.UserLendAndRevertMapper;
import com.cai.library.mapper.user.UserLibraryMapper;
import com.cai.library.pojo.GetPageOffsetRows;
import com.cai.library.pojo.HotBookRedisTool;
import com.cai.library.pojo.MD5;
import com.cai.library.pojo.RedisTool;
import com.cai.library.service.BookImgService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class Library2ApplicationTests {
    @Autowired
    BookMapper bookmapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserLibraryMapper userLibraryMapper;
    @Autowired
    CreateUuid createUuid;

    @Autowired
    MD5 md5;
    @Autowired
    RedisTool redisTool;
    @Autowired
    BorrowOfUserMapper borrowOfUserMapper;
    @Autowired
    GetPageOffsetRows getPageOffsetRows;
    @Autowired
    UserLendAndRevertMapper userLendAndRevertMapper;
    @Autowired
    HotBookRedisTool hotBookRedisTool;
    @Autowired
    BookMapper bookMapper;






    @Test
    void contextLoads() throws FileNotFoundException {
        String s = bookmapper.selectFormatById("10e064c0-f92d-4d92-90e4-236c42cb80dd");
        System.out.println(s);
        if (s==null){
            System.out.println("kong");
        }
    }
        @Test
        void reText() throws Exception {


            Map<String,Object> map = new HashMap();
            map.put("kindIndex",0);
            map.put("noKindSelectText","维特根斯坦");
            for (LibraryBook libraryBook : userLibraryMapper.selectLibraryBookByAny(map)) {
                System.out.println(libraryBook);
            }


        }

        @Test
        void Text() throws JsonProcessingException {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id","dca6f0fd-08a5-47eb-9947-924db659a901");
        map.put("borrow",1);
            List<Integer> integers = borrowOfUserMapper.selectActNumberByIdAndBorrowMap(map);
            for (Integer integer : integers) {
                System.out.println(integer);
            }
        }







    }

