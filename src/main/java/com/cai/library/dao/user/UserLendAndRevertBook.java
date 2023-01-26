package com.cai.library.dao.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLendAndRevertBook {
    private String id;
    private String bookId;
    //书名
    private String bookTitle;
    //作者
    private String author;
    //借/还
    private Integer borrowOrLend;
    //库存
    private Integer stock;
    //行为数量
    private Integer actNumber;
    //实际借书数量
    private  Integer actualBorrowingQuantity=0;
    //行为日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actDate;
}
