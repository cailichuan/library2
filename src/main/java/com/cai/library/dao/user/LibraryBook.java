package com.cai.library.dao.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryBook {
    //图书id
    private String id;
    //书名
    private String bookTitle;
    //作者
    private String author;

    //分类名
    private String kindString;
    //库存
    private int stock;
    //借阅数量
    private Integer lend;
}
