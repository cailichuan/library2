package com.cai.library.controller.admin;

import com.cai.library.dao.admin.BorrowOfUser;
import com.cai.library.dao.admin.PageInput;
import com.cai.library.exception.ServiceException;
import com.cai.library.pojo.GetPageInputOrMaxPage;
import com.cai.library.pojo.GetPageOffsetRows;
import com.cai.library.pojo.VerificationTool;
import com.cai.library.service.admin.BorrowOfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BorrowOfUserController {
    @Autowired
    BorrowOfUserService borrowOfUserService;
    @Autowired
    VerificationTool verificationTool;

    @Autowired
    GetPageInputOrMaxPage getPageInputOrMaxPage;

    @Autowired
    GetPageOffsetRows getPageOffsetRows;



    //根据页数显示信息词条
    @RequestMapping("/admin/UBI/{page}")
    public String selectBooksByPage(@PathVariable int page,
                                    Model model,
                                    HttpServletRequest request){
        int borrowListSize=0;
        //获取该页数据和翻页组件
        Map<String, Object> thisPageDataAndPageInputMap = borrowOfUserService.selectBorrowByPage(page);
        List<BorrowOfUser> borrowOfUsers = null;
        PageInput pageInput=null;

        //该页数据和翻页组件为空值处理
        if (thisPageDataAndPageInputMap==null){
            model.addAttribute("wmsg","没有数据");
            return "borrowofuser/borrow";
        }else {
            borrowOfUsers = (List<BorrowOfUser>)thisPageDataAndPageInputMap.get("thisPageData");
            pageInput=(PageInput) thisPageDataAndPageInputMap.get("PageInput");
            //将该页数据传到前端
            model.addAttribute("borrowlist",borrowOfUsers);
            //将翻页组件传到前端
            model.addAttribute("pageInput",pageInput);
        }

        //获取此页数据大小，并设置session,该数据用于某页删除最后一条数据，页面报无数据的情况
        if (borrowOfUsers==null && pageInput==null){
            model.addAttribute("wmsg","没有数据");
            return "borrowofuser/borrow";
        }else {
            HttpSession session = request.getSession(true);
            session.removeAttribute("borrowListSize");
            session.setAttribute("borrowListSize",borrowOfUsers.size());
        }



        return "borrowofuser/borrow";
    }

    //跳转到修改页
    @RequestMapping ("/admin/UBI/{page}/edit")
    public String editBorrowHtml(@PathVariable int page,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                 @RequestParam(name = "date", required = false)
                                  Date date,
                               @RequestParam(name = "id") String id,
                               @RequestParam(name = "borrow") int borrow,
                               Model model,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes){



        //通过url获取要修改的数据并封装进idAndBorrowMap
        Map<String,Object> idAndBorrowMap=new HashMap<>();
        idAndBorrowMap.put("id",id);
        idAndBorrowMap.put("borrow",borrow);
        if (borrow==0){
           idAndBorrowMap.put("date",date);
        }


        //获取要修改的信息
        BorrowOfUser editInfo = borrowOfUserService.getEditInfo(idAndBorrowMap);
        HttpSession session = request.getSession(true);
        session.removeAttribute("originalActNumber");
        session.setAttribute("originalActNumber",editInfo.getActNumber());

        model.addAttribute("editInfo",editInfo);
        model.addAttribute("returnPage",page);
        return "borrowofuser/edit";
    }



    //提交修改数据
    @RequestMapping ("/admin/UBI/{page}/edit/submit")
    public String editSubmit(@PathVariable int page,
                             BorrowOfUser borrow,
                             HttpServletRequest request,
                             RedirectAttributes rattributes){








        //验证输入的数据的基本合理性
        if (borrow.getUserId().equals("")||
                borrow.getBookId().equals("")||
                borrow.getActNumber()<1||
                borrow.getDate()==null
        ){
            rattributes.addFlashAttribute("msg","修改失败，注意输入格式");
            return "redirect:/admin/UBI/"+page+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
        }

        //如果borrow==1,则有修改图书id的或者用户id的需要
        if (borrow.getBorrow()==1){

            if (!verificationTool.bookIdVerification(borrow.getBookId())) {
                rattributes.addFlashAttribute("editInfo",borrow);
                rattributes.addFlashAttribute("msg","修改失败，图书id不存在");
                return "redirect:/admin/UBI/"+page+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
            }
            if (!verificationTool.userIdVerification(borrow.getUserId())){
                rattributes.addFlashAttribute("editInfo",borrow);
                rattributes.addFlashAttribute("msg","修改失败，用户id不存在");
                return "redirect:/admin/UBI/"+page+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
            }
        }

        //验证输入的行为数量是否合理
        //获取原先的atcNumber,并计算增量
        HttpSession session = request.getSession(false);
        int originalActNumber = (Integer) session.getAttribute("originalActNumber");
        int increment = borrow.getActNumber()-originalActNumber;
        if (!verificationTool.actNumberVerification(borrow,increment)){
            rattributes.addFlashAttribute("editInfo",borrow);
            rattributes.addFlashAttribute("msg","修改失败，注意行为数量是否符合逻辑，如，还书数量之和不能大于借书数量");
            return "redirect:/admin/UBI/"+page+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
        }

        //验证输入的时间是否合理
        if (!verificationTool.dateVerification(borrow.getId(),borrow.getDate(),borrow.getBorrow())&&
        !(borrow.getDate().getTime()==borrow.getOriginalDate().getTime())
        ) {
            rattributes.addFlashAttribute("editInfo",borrow);
            rattributes.addFlashAttribute("msg","修改失败，注意输入的时间是否合理，如借书时间要早于还书时间！");
            return "redirect:/admin/UBI/"+page+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
        }



        try {
            borrowOfUserService.upDateBorrow(borrow,increment);
            rattributes.addFlashAttribute("smsg","修改成功");
            return "redirect:/admin/UBI/"+page;
        }
        catch (ServiceException e){
            System.out.println("已经是修改失败");
            rattributes.addFlashAttribute("editInfo",borrow);
            rattributes.addFlashAttribute("msg","修改失败");
            return "redirect:/admin/UBI/"+page+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
        }





    }




    //跳转到添加借书信息页
    @RequestMapping("/admin/UBI/{page}/add")
    public String addBorrowHtml(@PathVariable int page,Model model){


        model.addAttribute("page",page);
        return "borrowofuser/add";
    }


    //提交添加的借书信息
    @GetMapping("/admin/UBI/{page}/add/submit")
    public String addSubmit(@PathVariable int page,BorrowOfUser borrow,RedirectAttributes rattributes){
        //将添加的数据设计为借
        borrow.setBorrow(1);

        //验证输入的数据的基本合理性
        if (    borrow.getUserId().equals("")||
                borrow.getBookId().equals("")||
                borrow.getActNumber()<1||
                borrow.getDate()==null
        ){
            rattributes.addFlashAttribute("msg","添加失败，注意输入格式");
            return "redirect:/admin/UBI/"+page+"/add";
        }

        //验证输入的借书数量是否合理，借书数量<书籍的库存总量
        if (!verificationTool.actNumberVerification(borrow)) {
            rattributes.addFlashAttribute("msg","输入的行为数量不符合逻辑!");
            return "redirect:/admin/UBI/"+page+"/add";
        }


        //验证输入的用户id是否存在
        if(!verificationTool.userIdVerification(borrow.getUserId())){
            rattributes.addFlashAttribute("msg","添加失败，该用户id不存在");
            return "redirect:/admin/UBI/"+page+"/add";
        }

        //验证输入的图书id是否存在
        if(!verificationTool.bookIdVerification(borrow.getBookId())){

            rattributes.addFlashAttribute("msg","添加失败，该图书id不存在");
            return "redirect:/admin/UBI/"+page+"/add";
        }



//        //数据库插入错误的处理
//        if (bookService.addBook(book)<1){
//            attributes.addFlashAttribute("msg","修改失败");
//            return "forward:/books/"+page+"/add";
//        }
//        attributes.addFlashAttribute("smsg","添加成功");


        try {
            borrowOfUserService.addBorrow(borrow);
            return "redirect:/admin/UBI/"+page;
        }catch (ServiceException e){
            rattributes.addFlashAttribute("msg","添加失败");
            return "redirect:/admin/UBI/"+page+"/add";
        }




    }



    //跳转到还书页
    @RequestMapping("/admin/UBI/{page}/revert")
    public String revertBorrowHtml(@PathVariable int page,
                                   HttpServletRequest request,
                                   @RequestParam(name = "id")String id,
                                   @RequestParam(name = "bookId")String bookId,
                                   @RequestParam(name = "userId")String userId,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   @RequestParam(name = "date",required = false) Date date,
                                   @RequestParam(name = "actNumber",required = false) Integer actNumber,
                                   Model model){



        model.addAttribute("userId",userId);
        model.addAttribute("bookId",bookId);
        model.addAttribute("revertId",id);

        if (date!=null){
            model.addAttribute("date",date);
        }

        if (actNumber!=null){
            model.addAttribute("actNumber",actNumber);
        }


        model.addAttribute("page",page);
        return "borrowofuser/add";
    }


    //还书信息提交
    @GetMapping("/admin/UBI/{page}/revert/submit")
    public String revertSubmit(@PathVariable int page,
                               BorrowOfUser borrow,
                               RedirectAttributes redirectAttributes){

        //设置为还书
        borrow.setBorrow(0);


        //验证还书时间的合理性
        if (borrow.getDate()==null || !verificationTool.dateVerification(borrow.getId(),borrow.getDate(),borrow.getBorrow())){
            redirectAttributes.addFlashAttribute("date",borrow.getDate());
            redirectAttributes.addFlashAttribute("actNumber",borrow.getActNumber());
            redirectAttributes.addFlashAttribute("msg","还书时间为空，或者比借书时间早");
            return "redirect:/admin/UBI/"+page+
                    "/revert?id="+
                    borrow.getId()+
                    "&userId="+
                    borrow.getUserId()+
                    "&bookId="+borrow.getBookId();
        }

        //验证还书数量的合理性
        if (!verificationTool.actNumberVerification(borrow)){
            redirectAttributes.addFlashAttribute("date",borrow.getDate());
            redirectAttributes.addFlashAttribute("actNumber",borrow.getActNumber());
            redirectAttributes.addFlashAttribute("msg","添加还书信息失败，行为数量不合逻辑，如，还书数量之和多于借书数量");
            return "redirect:/admin/UBI/"+page+
                    "/revert?id="+
                    borrow.getId()+
                    "&userId="+
                    borrow.getUserId()+
                    "&bookId="+borrow.getBookId();
        }


        try {
            borrowOfUserService.addBorrow(borrow);
            return "redirect:/admin/UBI/"+page;
        }

        catch (ServiceException e){
            redirectAttributes.addFlashAttribute("msg","添加还书信息失败");
            return "redirect:/admin/UBI/"+page+
                    "/revert?id="+
                    borrow.getId()+
                    "&userId="+
                    borrow.getUserId()+
                    "&bookId="+borrow.getBookId()+
                    "&date="+borrow.getDate()+
                    "&actNumber="+borrow.getActNumber();
        }







    }


    //删除数据
    @RequestMapping("/admin/UBI/{page}/delete/submit")
    public String delectSubmit(
            @PathVariable int page,
            @RequestParam(name = "id")String id,
            @RequestParam(name = "borrow",required = false)Integer borrow,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(name = "date",required = false) Date date,
            @RequestParam(name = "bookId",required = false) String bookId,
            @RequestParam(name = "actNumber",required = false)  Integer actNumber,
            HttpServletRequest request,
            RedirectAttributes attributes){

        BorrowOfUser borrowOfUser = new BorrowOfUser();
        borrowOfUser.setId(id);
        if (borrow!=null){
            borrowOfUser.setBorrow(borrow);
        }

        if (date!=null){
            borrowOfUser.setDate(date);
        }

        if (bookId!=null) {
            borrowOfUser.setBookId(bookId);
        }

        if (actNumber!=null){
            borrowOfUser.setActNumber(actNumber);
        }


        try {
            borrowOfUserService.deleteBorrow(borrowOfUser);
            //如果该数据是该页面最后一条数据，则返回上一页
            HttpSession session = request.getSession(false);
            int borrowListSize = (int)session.getAttribute("borrowListSize");
            if (borrowListSize-1==0){
                page--;
            }

            attributes.addFlashAttribute("smsg","删除成功");

            return "redirect:/admin/UBI/"+page;
        }
        catch (ServiceException e){
            attributes.addFlashAttribute("wmsg","删除失败");
            return "redirect:/admin/UBI/"+page;
        }


    }



    //提交搜索信息
    @GetMapping("/admin/UBI/{page}/select/{spage}")
    public String selectBookByAny(
            @PathVariable int page ,
            @PathVariable int spage,
            @RequestParam(name = "selectKindValue",required = false) Integer selectKindValue,
            @RequestParam(name = "selectTxt",required = false) String selectTxt,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            Model model
    ){



        //搜索框输入为空处理
        if (selectTxt.equals("")){
            redirectAttributes.addFlashAttribute("wmsg","搜索值不能为空");
            return "redirect:/admin/UBI/"+page;
        }


        //获取翻页按钮和翻页组件
        Map<String, Object> selectBookByAnyOrPageInputMap = borrowOfUserService.selectBorrowByAny(selectTxt, selectKindValue, spage);

        //搜索结果为空处理
        if (selectBookByAnyOrPageInputMap==null){
            redirectAttributes.addFlashAttribute("smsg","搜索结果为空");
            return "redirect:/admin/UBI/"+page;
        }
        //搜索成功
        List<BorrowOfUser> sBorrows = (List<BorrowOfUser>)selectBookByAnyOrPageInputMap.get("sBorrows");
        PageInput pageInput=(PageInput)selectBookByAnyOrPageInputMap.get("pageInput");
        //记录搜索前页数，用于返回按钮
        model.addAttribute("bPage",page);

        //传送搜索出来的数据与及翻页组件
        model.addAttribute("sBorrows",sBorrows);
        model.addAttribute("pageInput",pageInput);
        model.addAttribute("selectKindValue",selectKindValue);
        model.addAttribute("selectTxt",selectTxt);


        HttpSession session = request.getSession(true);
        //删除原有的session
        session.removeAttribute("selectKindValue");
        session.removeAttribute("selectTxt");
        session.removeAttribute("sborrowListSize");
        //session保存搜索字段，以及本页数据数量（用于删除本页最后一条数据返回上一页）
        session.setAttribute("selectKindValue",selectKindValue);
        session.setAttribute("selectTxt",selectTxt);
        session.setAttribute("sborrowListSize",sBorrows.size());

        return "borrowofuser/borrow";
    }



    //跳转到搜索还书页:8080/UBI/1/select/1/revert?
    @RequestMapping("/admin/UBI/{page}/select/{spage}/revert")
    public String selectRevertBorrowHtml(@PathVariable int page,
                                         @PathVariable int spage,
                                   @RequestParam(name = "id")String id,
                                   @RequestParam(name = "bookId")String bookId,
                                   @RequestParam(name = "userId")String userId,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   @RequestParam(name = "date",required = false) Date date,
                                   @RequestParam(name = "actNumber",required = false) Integer actNumber,
                                         HttpServletRequest request,
                                         Model model){



        model.addAttribute("userId",userId);
        model.addAttribute("bookId",bookId);
        model.addAttribute("selectReturnPage",spage);
        model.addAttribute("revertId",id);

        if (date!=null){
            model.addAttribute("date",date);
        }

        if (actNumber!=null){
            model.addAttribute("actNumber",actNumber);
        }


        model.addAttribute("page",page);
        return "borrowofuser/add";
    }


    //搜索还书信息提交
    @GetMapping("/admin/UBI/{page}/select/{spage}/revert/submit")
    public String selectRevertSubmit(@PathVariable int page,
                               @PathVariable int spage,
                               BorrowOfUser borrow,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {

        //设置为还书
        borrow.setBorrow(0);


        //验证还书时间的合理性
        if (borrow.getDate()==null || !verificationTool.dateVerification(borrow.getId(),borrow.getDate(),borrow.getBorrow())){
            redirectAttributes.addFlashAttribute("date",borrow.getDate());
            redirectAttributes.addFlashAttribute("actNumber",borrow.getActNumber());
            redirectAttributes.addFlashAttribute("msg","还书时间为空，或者比借书时间早");
            return "redirect:/admin/UBI/"+page+
                    "/select/"+spage+
                    "/revert?id="+
                    borrow.getId()+
                    "&userId="+
                    borrow.getUserId()+
                    "&bookId="+borrow.getBookId();
        }

        //验证还书数量的合理性
        if (!verificationTool.actNumberVerification(borrow)){
            redirectAttributes.addFlashAttribute("date",borrow.getDate());
            redirectAttributes.addFlashAttribute("actNumber",borrow.getActNumber());
            redirectAttributes.addFlashAttribute("msg","添加还书信息失败，行为数量不合逻辑，如，还书数量之和多于借书数量");
            return "redirect:/admin/UBI/"+page+
                    "/select/"+spage+
                    "/revert?id="+
                    borrow.getId()+
                    "&userId="+
                    borrow.getUserId()+
                    "&bookId="+borrow.getBookId();
        }


        try {
            borrowOfUserService.addBorrow(borrow);
            //获取搜索关键字
            HttpSession session = request.getSession(false);
            String selectTxt = (String)session.getAttribute("selectTxt");
            int selectKindValue = (int) session.getAttribute("selectKindValue");

            return "redirect:/admin/UBI/"+page+"/select/"+spage+
                    "?selectKindValue="+selectKindValue+
                    "&selectTxt="+ URLEncoder.encode(selectTxt,"utf-8");

        }catch (ServiceException e){
            redirectAttributes.addFlashAttribute("msg","添加还书信息失败");
            return "redirect:/admin/UBI/"+page+
                    "/select/"+spage+
                    "/revert?id="+
                    borrow.getId()+
                    "&userId="+
                    borrow.getUserId()+
                    "&bookId="+borrow.getBookId();

        }







    }




    //搜索删除数据
    @RequestMapping("/admin/UBI/{page}/select/{spage}/delete/submit")
    public String selectDelectSubmit(
            @PathVariable int page,
            @PathVariable int spage,
            @RequestParam(name = "id")String id,
            @RequestParam(name = "borrow",required = false)Integer borrow,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(name = "date",required = false) Date date,
            HttpServletRequest request,
            RedirectAttributes attributes) throws UnsupportedEncodingException {

        BorrowOfUser borrowOfUser = new BorrowOfUser();
        borrowOfUser.setId(id);
        if (borrow!=null){
            borrowOfUser.setBorrow(borrow);
        }
        if (date!=null){
            borrowOfUser.setDate(date);
        }

        try {
            borrowOfUserService.deleteBorrow(borrowOfUser);
            //如果该数据是该页面最后一条数据，则返回上一页
            HttpSession session = request.getSession(false);
            int borrowListSize = (int)session.getAttribute("borrowListSize");
            if (borrowListSize-1==0){
                page--;
            }

            attributes.addFlashAttribute("smsg","删除成功");

            //获取搜索页关键字session
            String selectTxt = (String)session.getAttribute("selectTxt");
            int selectKindValue = (int) session.getAttribute("selectKindValue");

            return "redirect:/admin/UBI/"+page+"/select/"+spage+
                    "?selectKindValue="+selectKindValue+
                    "&selectTxt="+ URLEncoder.encode(selectTxt,"utf-8");
        }
        catch (ServiceException e){
            attributes.addFlashAttribute("wmsg","删除失败");
            return "redirect:/admin/UBI/"+page;
        }



    }




    //跳转到修改页
    @RequestMapping ("/admin/UBI/{page}/select/{spage}/edit")
    public String selectEditBorrowHtml(@PathVariable int page,
                                 @PathVariable int spage,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                 @RequestParam(name = "date", required = false)
                                 Date date,
                                 @RequestParam(name = "id") String id,
                                 @RequestParam(name = "borrow") int borrow,
                                 Model model,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes){


        //通过url获取要修改的数据并封装进idAndBorrowMap
        Map<String,Object> idAndBorrowMap=new HashMap<>();
        idAndBorrowMap.put("id",id);
        idAndBorrowMap.put("borrow",borrow);
        if (borrow==0){
            idAndBorrowMap.put("date",date);
        }


        //获取要修改的信息
        BorrowOfUser editInfo = borrowOfUserService.getEditInfo(idAndBorrowMap);
        HttpSession session = request.getSession(true);
        session.removeAttribute("originalActNumber");
        session.setAttribute("originalActNumber",editInfo.getActNumber());

        model.addAttribute("selectEditInfo",editInfo);
        model.addAttribute("returnPage",page);
        model.addAttribute("selectReturnPage",spage);
        return "borrowofuser/edit";
    }



    //提交修改数据
    @RequestMapping ("/admin/UBI/{page}/select/{spage}/edit/submit")
    public String selectEditSubmit(@PathVariable int page,
                             @PathVariable int spage,
                             BorrowOfUser borrow,
                             HttpServletRequest request,
                             RedirectAttributes rattributes) throws UnsupportedEncodingException {








        //验证输入的数据的基本合理性
        if (borrow.getUserId().equals("")||
                borrow.getBookId().equals("")||
                borrow.getActNumber()<1||
                borrow.getDate()==null
        ){
            rattributes.addFlashAttribute("msg","修改失败，注意输入格式");
            return "redirect:/admin/UBI/"+page+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
        }

        //如果borrow==1,则有修改图书id的或者用户id的需要
        if (borrow.getBorrow()==1){

            if (!verificationTool.bookIdVerification(borrow.getBookId())) {
                rattributes.addFlashAttribute("selectEditInfo",borrow);
                rattributes.addFlashAttribute("msg","修改失败，图书id不存在");
                return "redirect:/admin/UBI/"+page+"/select/"+spage+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
            }
            if (!verificationTool.userIdVerification(borrow.getUserId())){
                rattributes.addFlashAttribute("selectEditInfo",borrow);
                rattributes.addFlashAttribute("msg","修改失败，用户id不存在");
                return "redirect:/admin/UBI/"+page+"/select/"+spage+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
            }
        }

        //验证输入的行为数量是否合理
        //获取原先的atcNumber,并计算增量
        HttpSession session = request.getSession(false);
        int originalActNumber = (Integer) session.getAttribute("originalActNumber");
        int increment = borrow.getActNumber()-originalActNumber;
        if (!verificationTool.actNumberVerification(borrow,increment)){
            rattributes.addFlashAttribute("selectEditInfo",borrow);
            rattributes.addFlashAttribute("msg","修改失败，注意行为数量是否符合逻辑，如，还书数量之和不能大于借书数量");
            return "redirect:/admin/UBI/"+page+"/select/"+spage+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
        }

        //验证输入的时间是否合理
        if (!verificationTool.dateVerification(borrow.getId(),borrow.getDate(),borrow.getBorrow())&&
                !(borrow.getDate().getTime()==borrow.getOriginalDate().getTime())
        ) {
            rattributes.addFlashAttribute("selectEditInfo",borrow);
            rattributes.addFlashAttribute("msg","修改失败，注意输入的时间是否合理，如借书时间要早于还书时间！");
            return "redirect:/admin/UBI/"+page+"/select/"+spage+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
        }


        try {
            borrowOfUserService.upDateBorrow(borrow,increment);
            String selectTxt = (String)session.getAttribute("selectTxt");
            int selectKindValue = (int) session.getAttribute("selectKindValue");
            rattributes.addFlashAttribute("smsg","成功");

            return "redirect:/admin/UBI/"+page+"/select/"+spage+
                    "?selectKindValue="+selectKindValue+
                    "&selectTxt="+ URLEncoder.encode(selectTxt,"utf-8");

        }

        catch (ServiceException e){
            rattributes.addFlashAttribute("selectEditInfo",borrow);
            rattributes.addFlashAttribute("msg","修改失败");
            return "redirect:/admin/UBI/"+page+"/select/"+spage+"/edit?id="+borrow.getId()+"&borrow="+borrow.getBorrow();
        }





    }
}
