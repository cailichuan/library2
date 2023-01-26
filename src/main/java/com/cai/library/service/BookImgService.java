package com.cai.library.service;

import com.cai.library.mapper.admin.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

@Service
public class BookImgService {
    @Autowired
    BookMapper bookMapper;
    public String getBookImg(String bookId) throws FileNotFoundException {
        String imgFormat="";
        String filename="";
        imgFormat = bookMapper.selectFormatById(bookId);
        if (imgFormat==null||imgFormat.equals("")){
            filename="111.png";
        }
        else {
            filename=bookId+"."+imgFormat;
        }


        return"/images/"+filename;
    }
}
