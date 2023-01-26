package com.cai.library.dataCreate;

import com.cai.library.dataCreate.pojo.CreateUuid;
import com.cai.library.mapper.admin.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateBook {
    @Autowired
    CreateUuid createUuid;

    @Autowired
    BookMapper bookMapper;
    public void addBook(){

    }
}
