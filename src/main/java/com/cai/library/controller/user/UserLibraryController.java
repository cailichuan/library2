package com.cai.library.controller.user;


import com.cai.library.dao.admin.Kind;
import com.cai.library.dao.admin.PageInput;
import com.cai.library.dao.user.LibraryBook;
import com.cai.library.exception.ServiceException;
import com.cai.library.service.admin.KindService;
import com.cai.library.service.user.UserLibraryService;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
public class UserLibraryController {
    @Autowired
    UserLibraryService userLibraryService;
    @Autowired
    KindService kindService;

    @RequestMapping("/user/libraryBook")
    public String to(){
        return "librarybook";
    }



    //根据页数显示借阅信息
    @RequestMapping("/user/libraryBook{kindIndex}/{page}")
    public String borrowAndLendHtml(@PathVariable int page,
                                    @PathVariable int kindIndex,
                                    @RequestParam(name = "wmsg",required = false) String wmsg,
                                    @RequestParam(name = "smsg",required = false) String smsg,
                                    HttpSession session,
                                    Model model
    ){



        //获取分类信息
        List<Kind> kinds = null;
        if(kindIndex<0){

            model.addAttribute("wmsg","没有此分类");
            return "userhtml/librarybook/librarybook";

        } else {
            kinds = kindService.selectAllKind();
            if (kindIndex>kinds.size()){
                model.addAttribute("wmsg","没有此分类");
                return "userhtml/librarybook/librarybook";
            }
        }



        Map<String, Object> thisPageDataAndPageInputAndKindMap = userLibraryService.selectBookByPage(page,kindIndex);
        List<LibraryBook> libraryBooks = null;
        PageInput pageInput= null;



        if (thisPageDataAndPageInputAndKindMap==null){
            model.addAttribute("wmsg","没有数据");
            return "userhtml/librarybook/librarybook";
        }else {
            libraryBooks =(List<LibraryBook>)thisPageDataAndPageInputAndKindMap.get("thisPageData");
            pageInput=(PageInput) thisPageDataAndPageInputAndKindMap.get("PageInput");
        }






        if (libraryBooks==null && pageInput == null){
            model.addAttribute("wmsg","没有数据");
            return "userhtml/librarybook/librarybook";
        }else {


            model.addAttribute("libraryBooklist",libraryBooks);
            //将翻页组件传到前端
            model.addAttribute("pageInput",pageInput);
            model.addAttribute("kindList",kinds);
            model.addAttribute("kindIndex",kindIndex);

        }

        return "userhtml/librarybook/librarybook";


    }




//    提交要搜索的数据
    @GetMapping("/user/libraryBook{kindIndex}/{page}/select/{spage}")
    public String selectBookByAny(
            @PathVariable int page ,
            @PathVariable int spage,
            @PathVariable int kindIndex,

            @RequestParam(name = "selectKindValue",required = false) Integer selectKindValue,
            @RequestParam(name = "selectTxt",required = false) String selectTxt,

            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            Model model
    ){



        //搜索框输入为空处理
        if (selectTxt.equals("")){
            model.addAttribute("msg","搜索值不能为空");
            return "userhtml/librarybook/nulllibrarybook";

        }

        //获取分类信息
        List<Kind> kinds = null;
        if(kindIndex<0){

            model.addAttribute("msg","没有此分类");
            return "userhtml/librarybook/nulllibrarybook";

        } else {
            kinds = kindService.selectAllKind();
            if (kindIndex>kinds.size()){
                model.addAttribute("msg","没有此分类");
                return "userhtml/librarybook/nulllibrarybook";
            }
        }


        //获取翻页按钮和翻页组件
        Map<String, Object> selectBookByAnyOrPageInputMap =userLibraryService.selectBookByAny(kindIndex,selectTxt, selectKindValue, spage);

        //搜索结果为空处理
        if (selectBookByAnyOrPageInputMap==null){
            model.addAttribute("msg","搜索结果为空");
            return "userhtml/librarybook/nulllibrarybook";
        }
        //搜索成功
        List<LibraryBook> libraryBooks = (List<LibraryBook>)selectBookByAnyOrPageInputMap.get("libraryBooks");
        PageInput pageInput=(PageInput)selectBookByAnyOrPageInputMap.get("pageInput");
        //记录搜索前页数，用于返回按钮
        model.addAttribute("bPage",page);

        //传送搜索出来的数据与及翻页组件
        model.addAttribute("slibraryBooks",libraryBooks);
        model.addAttribute("pageInput",pageInput);
        model.addAttribute("selectKindValue",selectKindValue);
        model.addAttribute("selectTxt",selectTxt);
        model.addAttribute("kindList",kinds);
        model.addAttribute("kindIndex",kindIndex);



        return "userhtml/librarybook/librarybook";
    }




    //跳转到非搜索借阅
    @RequestMapping("/user/libraryBook{kindIndex}/{page}/borrow")
    public String userBorrowHtml(
            @PathVariable int kindIndex,
            @PathVariable int page,
            @RequestParam(name = "bookId",required = false) String bookId,
            @RequestParam(name = "bookTitle",required = false) String bookTitle,
            @RequestParam(name = "stock",required = false) Integer stock,
            Model model
    ){


        if (bookId!=null && bookTitle!=null && (stock!=null || stock !=0)){
            model.addAttribute("bookId",bookId);
            model.addAttribute("bookTitle",bookTitle);
            model.addAttribute("stock",stock);
            model.addAttribute("page",page);
            model.addAttribute("kindIndex",kindIndex);
        }else {
            model.addAttribute("msg","传入的参数不合理");
        }



        return "userhtml/librarybook/bookborrow";
    }




    //提交非搜索借阅
    @GetMapping ("/user/libraryBook{kindIndex}/{page}/borrow/sumbit")
    public String  userBorrowSumbit(
            @PathVariable int kindIndex,
            @PathVariable int page,
            LibraryBook libraryBook,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws UnsupportedEncodingException {

        System.out.println("传过来的书籍="+libraryBook);
        //验证输入基本输入是否正确
        if(libraryBook.getLend()==null){

            redirectAttributes.addFlashAttribute("wmsg","借书数量不能为空");
            return "redirect:/user/libraryBook"+kindIndex+"/"+page+"/borrow?bookId="+libraryBook.getId()+
                    "&bookTitle="+ URLEncoder.encode(libraryBook.getBookTitle(),"utf-8")+
                    "&stock="+libraryBook.getStock();


        }

        if (libraryBook.getStock()<libraryBook.getLend()
                || libraryBook.getLend()<1){
            System.out.println("借书数量不得大于库存或者借书数量不能小于1");
            redirectAttributes.addFlashAttribute("wmsg","借书数量不得大于库存或者借书数量不能小于1");
            return "redirect:/user/libraryBook"+kindIndex+"/"+page+"/borrow?bookId="+libraryBook.getId()+
                    "&bookTitle="+ URLEncoder.encode(libraryBook.getBookTitle(),"utf-8")+
                    "&stock="+libraryBook.getStock();

        }
        //获取用户id
        String userId = (String)session.getAttribute("userId");

        try {
            userLibraryService.addBorrow(libraryBook,userId);
            redirectAttributes.addFlashAttribute("smsg","借阅成功");
            return "redirect:/user/libraryBook"+kindIndex+"/"+page;
        }
        catch (ServiceException e){
            redirectAttributes.addFlashAttribute("wmsg","借阅失败！");
            return "redirect:/user/libraryBook"+kindIndex+"/"+page+"/borrow?bookId="+libraryBook.getId()+
                    "&bookTitle="+ URLEncoder.encode(libraryBook.getBookTitle(),"utf-8")+
                    "&stock="+libraryBook.getStock();

        }



    }



    //跳转到搜素借阅
    @RequestMapping("/user/libraryBook{kindIndex}/{page}/select/{spage}/borrow")
    public String userSelectBorrowHtml(
            @PathVariable int kindIndex,
            @PathVariable int page,
            @PathVariable int spage,
            @RequestParam(name = "sBookId",required = false) String bookId,
            @RequestParam(name = "sBookTitle",required = false) String bookTitle,
            @RequestParam(name = "sStock",required = false) Integer stock,
            Model model
    ){
        if (bookId!=null && bookTitle!=null && (stock!=null || stock!=0)) {
            model.addAttribute("sBookId", bookId);
            model.addAttribute("sBookTitle", bookTitle);
            model.addAttribute("sStock", stock);
            model.addAttribute("page", page);
            model.addAttribute("spage", spage);
            model.addAttribute("kindIndex", kindIndex);
        }else {
            model.addAttribute("msg","传入的参数不合理");
        }



        return "userhtml/librarybook/bookborrow";
    }




    //提交搜索借阅
    @RequestMapping("/user/libraryBook{kindIndex}/{page}/select/{spage}/borrow/sumbit")
    public String  useSelectBorrowSumbit(
            @PathVariable int kindIndex,
            @PathVariable int page,
            @PathVariable int spage,
            LibraryBook libraryBook,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws UnsupportedEncodingException {


        //验证输入基本输入是否正确
        if(libraryBook.getLend()==null){
            redirectAttributes.addFlashAttribute("msg","借书数量不能为空");
            return "redirect:/user/libraryBook"+kindIndex+"/"+page+"/select/"+spage+
                    "/borrow?sBookId="+libraryBook.getId()+
                    "&sBookTitle="+URLEncoder.encode(libraryBook.getBookTitle(),"utf-8")+
                    "&sStock="+libraryBook.getStock();

        }

        if (libraryBook.getStock()<libraryBook.getLend()
                || libraryBook.getLend()<1){

            redirectAttributes.addFlashAttribute("msg","借书数量不得大于库存或者借书数量不能小于1");
            return "redirect:/user/libraryBook"+kindIndex+"/"+page+"/select/"+spage+
                    "/borrow?sBookId="+libraryBook.getId()+
                    "&sBookTitle="+URLEncoder.encode(libraryBook.getBookTitle(),"utf-8")+
                    "&sStock="+libraryBook.getStock();
        }

        //获取用户id
        String userId = (String)session.getAttribute("userId");

        try {
            userLibraryService.addBorrow(libraryBook,userId);
            redirectAttributes.addFlashAttribute("smsg","借书成功。");
            return "redirect:/user/libraryBook"+kindIndex+"/"+page+"/select/"+spage;

        }
        catch (ServiceException e){
            redirectAttributes.addFlashAttribute("msg","借阅失败！");
            return "redirect:/user/libraryBook"+kindIndex+"/"+page+"/borrow?bookId="+libraryBook.getId()+
                    "&bookTitle="+ URLEncoder.encode(libraryBook.getBookTitle(),"utf-8")+
                    "&stock="+libraryBook.getStock();

        }







    }




}
