package com.cai.library.pojo;

import com.cai.library.dao.admin.PageInput;
import org.springframework.stereotype.Component;

@Component
public class GetPageInputOrMaxPage {

    //获取翻页插件数据的方法
    //tisPage:当前页的页码，dataNumber:所有数据的总数
    public PageInput getPageInput(int tisPage,int dataNumber){
        PageInput pageInput=new PageInput();
        //调用类内方法获取所有数据的总页数
        int maxPage=getMaxPage(dataNumber);
        pageInput.setThisPage(tisPage);
        pageInput.setMaxPage(maxPage);
        return pageInput;
    }


    //获取最大页数的方法
    //dataNumber:所有数据的总数
    public int getMaxPage(int dataNumber){
        int maxPage=dataNumber/8;
        if (dataNumber%8!=0){
            maxPage++;
        }

        return maxPage;
    }
}
