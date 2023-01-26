package com.cai.library.service.admin;

import com.cai.library.dao.admin.Kind;
import com.cai.library.mapper.admin.KindMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KindService {
    @Autowired
    KindMapper mapper;
    public List<Kind> selectAllKind(){
        List<Kind> kinds = mapper.selectAllKind();
        return kinds;
    }
}
