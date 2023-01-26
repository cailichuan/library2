package com.cai.library.service.admin;

import com.cai.library.dao.admin.HotBook;
import com.cai.library.pojo.HotBookRedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotBookService {
    @Autowired
    HotBookRedisTool hotBookRedisTool;
    public List<HotBook> getHotBooks(){


        return hotBookRedisTool.getHotBook();

    }
}
