package com.cai.library.mapper.admin;

import com.cai.library.dao.admin.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface BookMapper {


    int addBook(Book book);
    int getDataNumber();



    int updateBook(Book book);

    int deleteBook(String id);

    int countSelectBookAny(Map selectTxt);
    int countSelectBookById(String id);
    int selectNumberById(String id);
    String selectTitleById(String id);
    String selectFormatById(String id);
    Book selectBookById(String id);
    List<Book> selectBookByAny(Map selectTxt);
    List<Book> selectByPage(Map offsetRowsMap);
    List<Book> selectAll();



}
