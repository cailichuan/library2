package com.cai.library.dao.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String id;
    private String title;
    private String author;
    private int kindNumber;
    private String kindString;

    private int number;
    //图片格式
    private String imgFormat;








}
