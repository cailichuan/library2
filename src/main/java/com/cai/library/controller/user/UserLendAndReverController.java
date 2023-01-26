package com.cai.library.controller.user;

import com.cai.library.dao.admin.BorrowOfUser;
import com.cai.library.dao.admin.PageInput;
import com.cai.library.dao.user.UserLendAndRevertBook;
import com.cai.library.exception.ServiceException;
import com.cai.library.pojo.VerificationTool;
import com.cai.library.service.user.UserLendAndRevertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


@Controller
public class UserLendAndReverController {

    @Autowired
    UserLendAndRevertService userLendAndRevertService;
    @Autowired
    VerificationTool verificationTool;








    //根据页数显示借阅信息
    @RequestMapping("/user/BAL/{page}")
    public String borrowAndLendHtml(@PathVariable int page,
                                    @RequestParam(name = "smsg",required = false)String smsg,

                                    HttpSession  session,
                                    Model model
    ){



        //获取用户id
        String userId = (String)session.getAttribute("userId");

        Map<String, Object> thisPageDataAndPageInputAndKindMap = userLendAndRevertService.selectBookByPage(page,userId);
        List<UserLendAndRevertBook> borrows = null;
        PageInput pageInput= null;



        if (thisPageDataAndPageInputAndKindMap==null){
            model.addAttribute("wmsg","没有数据");

        }else {
            borrows =(List<UserLendAndRevertBook>)thisPageDataAndPageInputAndKindMap.get("thisPageData");
            for (UserLendAndRevertBook borrow : borrows) {
                System.out.println(borrow);
            }
            pageInput=(PageInput) thisPageDataAndPageInputAndKindMap.get("PageInput");

        }






        if (borrows==null && pageInput == null){
            model.addAttribute("wmsg","没有数据");

        }else {

            model.addAttribute("borrowlist",borrows);
            //将翻页组件传到前端
            model.addAttribute("pageInput",pageInput);
            //获取此页数据大小，并设置session,该数据用于某页删除最后一条数据，页面报无数据的情况

            session.removeAttribute("borrowListSize");
            session.setAttribute("borrowListSize",borrows.size());
        }

        return "userhtml/mylendandrevert/mylendandrevert";


    }


    //跳转到还书页
    @RequestMapping("/user/BAL/{page}/revert")
    public String revertHtml(
            @PathVariable int page,
            UserLendAndRevertBook userLendAndRevertBook,
            Model model
    ){
        model.addAttribute("userLendAndRevertBook",userLendAndRevertBook);
        model.addAttribute("page",page);




        model.addAttribute("actualBorrowingQuantity",userLendAndRevertBook.getActualBorrowingQuantity());

        return "userhtml/mylendandrevert/revert";
    }



    //还书
    @GetMapping("/user/BAL/{page}/revert/submit")
    public String revertSubmit(
            @PathVariable Integer page,
            HttpSession session,
            UserLendAndRevertBook book,
            RedirectAttributes attributes
    ) throws UnsupportedEncodingException {

        System.out.println("sssb");

        if (book.getBookId()==null || book.getId().equals("")){
            attributes.addFlashAttribute("wmsg","操作失败");
            return "redirect:/user/BAL/"+page+"/revert/submit?id="+book.getId()+
                    "&bookId="+book.getBookId()+
                    "&bookTitle"+ URLEncoder.encode(book.getBookTitle(),"utf-8")+
                    "&stock="+book.getStock()+
                    "&actualBorrowingQuantity"+book.getActualBorrowingQuantity()+
                    "&actNumber"+book.getActNumber();
        }

        if (book.getStock()==null ||
                book.getBookTitle()==null || book.getBookTitle().equals("")||
        book.getStock()==null ||
        book.getActualBorrowingQuantity()==null ||
        book.getActNumber()==null){
            attributes.addFlashAttribute("wmsg","输入有空");
            return "redirect:/user/BAL/"+page+"/revert/submit?id="+book.getId()+
                    "&bookId="+book.getBookId()+
                    "&bookTitle"+ URLEncoder.encode(book.getBookTitle(),"utf-8")+
                    "&stock="+book.getStock()+
                    "&actualBorrowingQuantity"+book.getActualBorrowingQuantity()+
                    "&actNumber"+book.getActNumber();

        }

        if (book.getActualBorrowingQuantity()<book.getActNumber() || book.getActNumber()<1){
            attributes.addFlashAttribute("msg","还书数量不得大于实际借书数量或者还书数量不得小于1");
            return "redirect:/user/BAL/"+page+"/revert/submit?id="+book.getId()+
                    "&bookId="+book.getBookId()+
                    "&bookTitle"+ URLEncoder.encode(book.getBookTitle(),"utf-8")+
                    "&stock="+book.getStock()+
                    "&actualBorrowingQuantity"+book.getActualBorrowingQuantity()+
                    "&actNumber"+book.getActNumber();



        }

        try {
            String userId="";
            if (session.getAttribute("userId")!=null){
               userId = (String) session.getAttribute("userId");
            }else {
                throw new ServiceException();
            }

            userLendAndRevertService.revertBook(book,userId);
            attributes.addFlashAttribute("smsg","还书成功！");
            return "redirect:/user/BAL/"+page;
        }catch (ServiceException e){
            attributes.addFlashAttribute("wmsg","借书失败");
            return "redirect:/user/BAL/"+page+"/revert/submit?id="+book.getId()+
                    "&bookId="+book.getBookId()+
                    "&bookTitle"+ URLEncoder.encode(book.getBookTitle(),"utf-8")+
                    "&stock="+book.getStock()+
                    "&actualBorrowingQuantity"+book.getActualBorrowingQuantity()+
                    "&actNumber"+book.getActNumber();
        }






    }






}
