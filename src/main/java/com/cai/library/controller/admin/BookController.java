package com.cai.library.controller.admin;

import com.cai.library.anntation.CurrentLimiting;
import com.cai.library.dao.admin.Book;
import com.cai.library.dao.admin.Kind;
import com.cai.library.dao.admin.PageInput;
import com.cai.library.exception.ServiceException;
import com.cai.library.pojo.GetPageInputOrMaxPage;
import com.cai.library.pojo.VerificationTool;
import com.cai.library.service.admin.BookService;
import com.cai.library.service.admin.FileUrlService;
import com.cai.library.service.admin.KindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
public class BookController {

    //装配书本信息业务层的类
    @Autowired
    BookService bookService;

    //装配书本分类业务层的类
    @Autowired
    KindService kindService;

    @Autowired
    FileUrlService fileUrlService;


    //装配获取翻页组件信息或者获取总数据最大页数的类
    @Autowired
    GetPageInputOrMaxPage getPageInputOrMaxPage;

    @Autowired
    VerificationTool verificationTool;




    //根据页数显示book页
    @CurrentLimiting(bucketName = "selectBooksByPage",maxCount = 1,createRate = 3)
    @RequestMapping("/admin/books/{page}")
    public String selectBooksByPage(@PathVariable int page , Model model,HttpServletRequest request){
        //获取该页数据和翻页组件
        Map<String, Object> thisPageDataAndPageInputMap = bookService.selectBookByPage(page);
        List<Book> books = null;
        PageInput pageInput=null;

        //该页数据和翻页组件为空值处理
        if (thisPageDataAndPageInputMap==null){
            model.addAttribute("wmsg","没有数据");
            return "books/book";
        }else {
            books = (List<Book>)thisPageDataAndPageInputMap.get("thisPageData");
            pageInput=(PageInput) thisPageDataAndPageInputMap.get("PageInput");

        }

        if (books==null && pageInput==null){
            model.addAttribute("wmsg","没有数据");
            return "books/book";
        }else {

            //将该页数据传到前端
            model.addAttribute("booklist",books);
            //将翻页组件传到前端
            model.addAttribute("pageInput",pageInput);
            //获取此页数据大小，并设置session,该数据用于某页删除最后一条数据，页面报无数据的情况
            HttpSession session = request.getSession(true);
            session.removeAttribute("bookListSize");
            session.setAttribute("bookListSize",books.size());
        }




        return "books/book";
    }




    //跳转到修改页
    @RequestMapping ("/admin/books/{page}/edit{id}")
    public String editBookHtml(@PathVariable int page,@PathVariable String id,Model model){
        Book editInfo = bookService.getEditInfo(id);
        List<Kind> kinds = kindService.selectAllKind();
        model.addAttribute("editInfo",editInfo);
        model.addAttribute("kinds",kinds);
        model.addAttribute("returnPage",page);
        return "books/edit";
    }

    //提交编辑信息
    @PostMapping("/admin/books/{page}/edit/submit")
    public String editSubmit(@PathVariable int page,
                             Book book,
                             @RequestBody(required = false) MultipartFile file,
                             RedirectAttributes attributes) throws FileNotFoundException {


        if (book.getTitle().equals("")||book.getAuthor().equals("")||book.getNumber()<1){
            attributes.addFlashAttribute("msg","修改失败");
            return "redirect:/admin/books/"+page+"/edit"+book.getId();
        }

        if (!verificationTool.numberOfBookVerification(book.getId(), book.getNumber())){
            attributes.addFlashAttribute("msg","图书总量不能比借出的图书数量少");
            return "redirect:/admin/books/"+page+"/edit"+book.getId();
        }

        try {
            bookService.upDateBook(book,file);
            attributes.addFlashAttribute("smsg","修改成功");

            return "redirect:/admin/books/"+page;

        }catch (ServiceException e){
            attributes.addFlashAttribute("msg","修改失败");
            return "redirect:/admin/books/"+page+"/edit"+book.getId();
        }






    }


    //跳转到添加信息页
    @RequestMapping("/admin/books/{page}/add")
    public String addBookHtml(@PathVariable int page,Model model){

        List<Kind> kinds = kindService.selectAllKind();
        model.addAttribute("page",page);
        model.addAttribute("kinds",kinds);
        return "books/add";
    }



    //提交添加数据
    @PostMapping("/admin/books/{page}/add/submit")
    public String addSubmit(@PathVariable int page,
                            Book book,
                            @RequestParam("file") MultipartFile file,

                            RedirectAttributes attributes) throws FileNotFoundException {


        //输入错误的处理
        if (book.getTitle().equals("")||book.getAuthor().equals("")||book.getNumber()<1){
            attributes.addFlashAttribute("msg","修改失败");
            return "redirect:/admin/books/"+page+"/add";
        }

        //数据库插入错误的处理
        try {
            bookService.addBook(book,file);
            attributes.addFlashAttribute("smsg","添加成功");

            return "redirect:/admin/books/"+page;
        }
        catch (FileNotFoundException e)
        {
            attributes.addFlashAttribute("msg",e.getMessage());
            return "redirect:/admin/books/"+page+"/add";
        }
        catch (ServiceException e){
            attributes.addFlashAttribute("msg","修改失败");
            return "redirect:/admin/books/"+page+"/add";
        }


    }



    //删除数据
    @RequestMapping("/admin/books/{page}/delete{id}/submit")
    public String delect(
            @PathVariable int page,
            @PathVariable String id,
            HttpServletRequest request,
            RedirectAttributes attributes){


        if (bookService.deleteBook(id)<1){
           attributes.addFlashAttribute("wmsg","删除失败");
        }else {

            //如果该数据是该页面最后一条数据，则返回上一页
            HttpSession session = request.getSession(false);
            int bookListSize = (int)session.getAttribute("bookListSize");
            if (bookListSize-1==0){
                page--;
            }

            attributes.addFlashAttribute("smsg","删除成功");
        }
        return "redirect:/admin/books/"+page;
    }



    //提交要搜索的数据
    @GetMapping("/admin/books/{page}/select/{spage}")
    public String selectBookByAny(
            @PathVariable int page ,
            @PathVariable int spage,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            Model model
            ){

        //通过url获取form表单的值
        int selectKindValue = Integer.parseInt(request.getParameter("selectKindValue"));
        String selectTxt = request.getParameter("selectTxt");

        //搜索框输入为空处理
        if (selectTxt.equals("")){
            redirectAttributes.addFlashAttribute("wmsg","搜索值不能为空");
            return "redirect:/admin/books/"+page;
        }


        //获取翻页按钮和翻页组件
        Map<String, Object> selectBookByAnyOrPageInputMap = bookService.selectBookByAny(selectTxt, selectKindValue, spage);

        //搜索结果为空处理
        if (selectBookByAnyOrPageInputMap==null){
            redirectAttributes.addFlashAttribute("smsg","搜索结果为空");
            return "redirect:/admin/books/"+page;
        }
        //搜索成功
        List<Book> sBooks = (List<Book>)selectBookByAnyOrPageInputMap.get("sbooks");
        PageInput pageInput=(PageInput)selectBookByAnyOrPageInputMap.get("pageInput");
        //记录搜索前页数，用于返回按钮
        model.addAttribute("bPage",page);

        //传送搜索出来的数据与及翻页组件
        model.addAttribute("sBooks",sBooks);
        model.addAttribute("pageInput",pageInput);
        model.addAttribute("selectKindValue",selectKindValue);
        model.addAttribute("selectTxt",selectTxt);


        HttpSession session = request.getSession(true);
        //删除原有的session
        session.removeAttribute("selectKindValue");
        session.removeAttribute("selectTxt");
        session.removeAttribute("sbookListSize");
        //session保存搜索字段，以及本页数据数量（用于删除本页最后一条数据返回上一页）
        session.setAttribute("selectKindValue",selectKindValue);
        session.setAttribute("selectTxt",selectTxt);
        session.setAttribute("sbookListSize",sBooks.size());

        return "books/book";
    }


    //跳转到搜索修改页
    @RequestMapping("/admin/books/{page}/select/{spage}/edit{id}")
    public String selectEditBookHtml(
            @PathVariable int page,
            @PathVariable int spage,
            @PathVariable String id,
            Model model){
        Book editInfo = bookService.getEditInfo(id);

        List<Kind> kinds = kindService.selectAllKind();




        model.addAttribute("selectEditInfo",editInfo);
        model.addAttribute("kinds",kinds);
        model.addAttribute("selectReturnPage",spage);
        model.addAttribute("page",page);


        return "books/edit";
    }

    //搜索编辑提交
    @PostMapping("/admin/books/{page}/select/{spage}/edit/submit")
    public String selectEditSubmit(
            @PathVariable int page,
            @PathVariable int spage, Book book,
            @RequestBody(required = false) MultipartFile file,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        //获取搜索关键字的session
        String selectTxt=null;
        int selectKindValue;
        HttpSession session = request.getSession(false);
        selectTxt = (String)session.getAttribute("selectTxt");
        selectKindValue = (int) session.getAttribute("selectKindValue");

        //输入校验
        if(book.getTitle().equals("") || book.getAuthor().equals("") || book.getNumber()<1 ){
            redirectAttributes.addFlashAttribute("msg","输入有误，请重新输入");
            return "redirect:/admin/books/"+page+"/select/"+spage+"/edit"+URLEncoder.encode(book.getId(),"utf-8");
        }
        //修改成功修改失败
        try {
            bookService.upDateBook(book,file);
            redirectAttributes.addFlashAttribute("smsg","修改成功");
        }catch (ServiceException e){
            redirectAttributes.addFlashAttribute("wmsg","修改失败");

        }finally {

            return "redirect:/admin/books/"+page+
                    "/select/"+spage+"?selectKindValue="+
                    selectKindValue+"&selectTxt="+
                    URLEncoder.encode(selectTxt,"utf-8");

        }

    }


    //搜索删除
    @RequestMapping("/admin/books/{page}/select/{spage}/delete{id}/submit")
    public String selectDeleteSubmit(
            @PathVariable int page,
            @PathVariable int spage,
            @PathVariable String id,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) throws UnsupportedEncodingException {
        //删除成功或不成功处理
        if(bookService.deleteBook(id)==1){

            //如果删除的是该页最后一条数据，则返回上一页
            HttpSession session = request.getSession(false);
            int sbookListSize = (int)session.getAttribute("sbookListSize");
            if (sbookListSize-1==0){
                spage--;
            }
            redirectAttributes.addFlashAttribute("smsg","删除成功");
        }else{

            redirectAttributes.addFlashAttribute("wmsg","删除失败");
        }

        //获取搜索页关键字session
        HttpSession session = request.getSession(false);
        String selectTxt = (String)session.getAttribute("selectTxt");
        int selectKindValue = (int) session.getAttribute("selectKindValue");

        return "redirect:/admin/books/"+page+"/select/"+spage+"?selectKindValue="+selectKindValue+"&selectTxt="+URLEncoder.encode(selectTxt,"utf-8");
    }





}
