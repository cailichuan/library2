package com.cai.library.controller.admin;

import com.cai.library.dao.admin.BorrowOfBook;
import com.cai.library.dao.admin.PageInput;
import com.cai.library.service.admin.BorrowOfBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class BorrowOfBookController {
    @Autowired
    BorrowOfBookService borrowOfBookService;

    //根据页数显示
    @RequestMapping("/admin/BBI/{page}")
    public String selectBooksByPage(@PathVariable int page , Model model, HttpServletRequest request){
        //获取该页数据和翻页组件
        Map<String, Object> thisPageDataAndPageInputMap = borrowOfBookService.selectBorrowByPage(page);
        List<BorrowOfBook> borrows = null;
        PageInput pageInput= null;

        if (thisPageDataAndPageInputMap==null){
            model.addAttribute("wmsg","没有数据");
            return "borrowofbook/borrow";
        }else {
            borrows =(List<BorrowOfBook>)thisPageDataAndPageInputMap.get("thisPageData");
            pageInput=(PageInput) thisPageDataAndPageInputMap.get("PageInput");

        }

        if (borrows==null && pageInput == null){
            model.addAttribute("wmsg","没有数据");
            return "borrowofbook/borrow";
        }else {

            model.addAttribute("borrowlist",borrows);
            //将翻页组件传到前端
            model.addAttribute("pageInput",pageInput);
            //获取此页数据大小，并设置session,该数据用于某页删除最后一条数据，页面报无数据的情况
            HttpSession session = request.getSession(true);
            session.removeAttribute("borrowListSize");
            session.setAttribute("borrowListSize",borrows.size());
        }

        return "borrowofbook/borrow";
    }

    //提交搜索信息
    @GetMapping("/admin/BBI/{page}/select/{spage}")
    public String selectBookByAny(
            @PathVariable int page ,
            @PathVariable int spage,

            @RequestParam(name = "selectTxt",required = false) String selectTxt,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            Model model
    ){



        //搜索框输入为空处理
        if (selectTxt.equals("")){
            redirectAttributes.addFlashAttribute("wmsg","搜索值不能为空");
            return "redirect:/admin/BBI/"+page;
        }


        //获取翻页按钮和翻页组件
        Map<String, Object> selectBookByAnyOrPageInputMap = borrowOfBookService.selectBorrowByAny(selectTxt, spage);

        //搜索结果为空处理
        if (selectBookByAnyOrPageInputMap==null){
            redirectAttributes.addFlashAttribute("smsg","搜索结果为空");
            return "redirect:/admin/BBI/"+page;
        }
        //搜索成功
        List<BorrowOfBook> sBorrows = (List<BorrowOfBook>)selectBookByAnyOrPageInputMap.get("sBorrows");
        PageInput pageInput=(PageInput)selectBookByAnyOrPageInputMap.get("pageInput");
        //记录搜索前页数，用于返回按钮
        model.addAttribute("bPage",page);

        //传送搜索出来的数据与及翻页组件
        model.addAttribute("sBorrows",sBorrows);
        model.addAttribute("pageInput",pageInput);

        model.addAttribute("selectTxt",selectTxt);


        return "borrowofbook/borrow";
    }
}
