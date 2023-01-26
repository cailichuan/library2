package com.cai.library.dao.admin;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageInput {

    private int thisPage;
    private int maxPage;


    //获取当前页
    public void setThisPage(int this_page){
        this.thisPage= this_page;
    }

    //获取表最大页数
    public void setMaxPage(int max_page){
        this.maxPage=max_page;
    }

    //获取翻页li里面的数值
    public List<Integer> getPageInput(){
        List<Integer> pageInput = new ArrayList<Integer>();
        int start=thisPage-thisPage%5;
        if (start<1){
            start=1;
        }
        int end=thisPage+(5-thisPage%5);
        if (end>maxPage){
            end=maxPage;
        }
        for (int i =start;i<=end;i++){
            pageInput.add(i);
        }

        return pageInput;
    }

    //获取上一页
    public int getLastPage(){
        int lastPage=thisPage-1;
        if (lastPage<1){
            lastPage=1;
        }
        return lastPage;
    }


    //获取下一页
    public int getNextPage(){
        int nextPage=thisPage+1;
        if (nextPage>maxPage){
            nextPage=maxPage;
        }
        return nextPage;
    }

}
