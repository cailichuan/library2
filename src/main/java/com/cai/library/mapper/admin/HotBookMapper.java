package com.cai.library.mapper.admin;

import com.cai.library.dao.admin.HotBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HotBookMapper {
    List<HotBook> selectHotBookTitleById(String id);
}
