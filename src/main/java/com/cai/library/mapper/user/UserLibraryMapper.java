package com.cai.library.mapper.user;

import com.cai.library.dao.user.LibraryBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserLibraryMapper {
    List<LibraryBook> selectLibraryBookByPage(Map map);
    int getDataNumber(int kindIndex);
    List<LibraryBook>selectLibraryBookByAny(Map map);
    int countLibraryBookAny(Map map);
}
