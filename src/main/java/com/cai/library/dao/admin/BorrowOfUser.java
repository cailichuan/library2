package com.cai.library.dao.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowOfUser {
    private String id;

    private String userId;

    private String userName;

    private String bookId;

    private String bookTitle;

    //判断该行为是借还是还，1为借，0为还
    private int borrow;
    //行为数量，即借或者还的数量
    private int actNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date originalDate;
}
