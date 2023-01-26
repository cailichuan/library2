package com.cai.library.dao.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowOfBook {
    private String id;
    private String bookTitle;
    //总数
    private int gross;
    //库存
    private int stock;
    //借出数量
    private int lend;

}
