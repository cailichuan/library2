package com.cai.library.controller.admin;

import com.cai.library.dao.admin.HotBook;
import com.cai.library.service.admin.HotBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HotBookController {
    @Autowired
    HotBookService hotBookService;

    //获取热度信息
    @RequestMapping("/admin/hotBook")
    public String getHotBook(
            Model model
    ){
        List<HotBook> hotBooks = hotBookService.getHotBooks();
        if (hotBooks==null) {
            model.addAttribute("wmsg","数据为空！");
        }else {
            model.addAttribute("hotBooks",hotBooks);
        }


        return "hotbooks/hotbooks";
    }
}
