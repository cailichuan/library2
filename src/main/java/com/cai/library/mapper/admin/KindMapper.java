package com.cai.library.mapper.admin;

import com.cai.library.dao.admin.Kind;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface KindMapper {
    List<Kind> selectAllKind();
    int countKindById(int id);
}
