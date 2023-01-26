package com.cai.library.dao.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotBook {
    private Integer ranking;
    private String id;
    private String bookTitle;
    private  Integer hot;
}
