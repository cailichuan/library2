package com.cai.library.pojo;

import com.cai.library.dao.admin.HotBook;
import com.cai.library.mapper.admin.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class HotBookRedisTool {
    @Autowired
    RedisTool redisTool;
    @Autowired
    BookMapper bookMapper;

    //点击借阅之后进行热度统计
    @Transactional
    public void  countHot(String bookId){
        String zSetKey = "hotbook";
        //判断在热度榜中存不存在该图书id
        if (redisTool.zSocre(zSetKey,bookId)==null) {
            //不存在则添加书籍
            redisTool.zadd(zSetKey,bookId,1d);
        }else {
            //存在则热度+1
            redisTool.zincrementScore(zSetKey,bookId,1d);
        }

    }

    @Transactional
    public List<HotBook> getHotBook(){
        List<HotBook> hotBooks = new ArrayList<>();

        //获取排名前十的书籍的id
        Set<String> hotbookIds = redisTool.zReverseRange("hotbook", 0, 10);
        int index= 0;
        for (String hotbookId : hotbookIds) {
            HotBook hotBook = new HotBook();
            hotBook.setId(hotbookId);
            hotBook.setBookTitle(bookMapper.selectTitleById(hotbookId));
            Integer hot= Math.toIntExact(Math.round(redisTool.zSocre("hotbook", hotbookId)));
            hotBook.setHot(hot);
            hotBook.setRanking(index+1);
            hotBooks.add(index++,hotBook);
        }

        return hotBooks;


    }
}
