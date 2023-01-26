package com.cai.library.controller.admin;

import com.cai.library.dao.admin.PageInput;
import com.cai.library.dao.admin.User;
import com.cai.library.exception.ServiceException;
import com.cai.library.pojo.ReTool;
import com.cai.library.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ReTool reTool;




    //根据页数显示book页
    @RequestMapping("/admin/users/{page}")
    public String selectBooksByPage(@PathVariable int page , Model model, HttpServletRequest request){
        //获取该页数据和翻页组件
        Map<String, Object> thisPageDataAndPageInputMap = userService.selectUserByPage(page);
        List<User> users = null;
        PageInput pageInput=null;

        //该页数据和翻页组件为空值处理
        if (thisPageDataAndPageInputMap==null){
            model.addAttribute("wmsg","没有数据");
            return "users/user";
        }else {
           users = (List<User>)thisPageDataAndPageInputMap.get("thisPageData");
            pageInput=(PageInput) thisPageDataAndPageInputMap.get("PageInput");
        }

        if (users==null&&pageInput==null){
            model.addAttribute("wmsg","没有数据");
            return "users/user";
        }else {
            //将该页数据传到前端
            model.addAttribute("userlist",users);
            //将翻页组件传到前端
            model.addAttribute("pageInput",pageInput);
            //获取此页数据大小，并设置session,该数据用于某页删除最后一条数据，页面报无数据的情况
            HttpSession session = request.getSession(true);
            session.removeAttribute("userListSize");
            session.setAttribute("userListSize",users.size());
        }




        return "users/user";
    }

    //跳转到修改页
    @RequestMapping ("/admin/users/{page}/edit{id}")
    public String editBookHtml(@PathVariable int page,@PathVariable String id,Model model){
        User editInfo = userService.getEditInfo(id);

        model.addAttribute("editInfo",editInfo);

        model.addAttribute("returnPage",page);
        return "users/edit";
    }

    //提交编辑信息
    @GetMapping("/admin/users/{page}/edit/submit")
    public String editSubmit(@PathVariable int page,User user, RedirectAttributes attributes){

        if (
                user.getName().equals("")||
                user.getPhoneNumber().equals("")||
                !reTool.phoneNumber(user.getPhoneNumber())){
            attributes.addFlashAttribute("msg","修改失败,请注意修改格式！");
            return "redirect:/admin/users/"+page+"/edit"+user.getId();
        }

        if (userService.upDateUser(user)<1){
            attributes.addFlashAttribute("msg","修改失败");
            return "redirect:/admin/users/"+page+"/edit"+user.getId();
        }

        attributes.addFlashAttribute("smsg","修改成功");

        return "redirect:/admin/users/"+page;
    }

    //跳转到添加信息页
    @RequestMapping("/admin/users/{page}/add")
    public String addBookHtml(@PathVariable int page,Model model){

        model.addAttribute("page",page);

        return "users/add";
    }

    //提交添加数据
    @RequestMapping("/admin/users/{page}/add/submit")
    public String addSubmit(@PathVariable int page,User user,RedirectAttributes attributes){

        //输入错误的处理
        //1(\\d){10}
        if (user.getName().equals("")||
                user.getPhoneNumber().equals("")||
                !reTool.phoneNumber(user.getPhoneNumber())
        ){
            attributes.addFlashAttribute("msg","添加失败，注意输入格式");
            return "redirect:/admin/users/"+page+"/add";
        }

        //数据库插入错误的处理
        if (userService.addUser(user)<1){
            attributes.addFlashAttribute("msg","添加失败");
            return "redirect:/admin/users/"+page+"/add";
        }
        attributes.addFlashAttribute("smsg","添加成功");

        return "redirect:/admin/users/"+page;
    }

    //删除数据
    @RequestMapping("/admin/users/{page}/delete{id}/submit")
    public String delect(
            @PathVariable int page,
            @PathVariable String id,
            HttpServletRequest request,
            RedirectAttributes attributes){


        try {
            userService.deleteUserById(id);
            //如果该数据是该页面最后一条数据，则返回上一页
            HttpSession session = request.getSession(false);
            int userListSize = (int)session.getAttribute("userListSize");
            if (userListSize-1==0 && page>1){
                page--;
            }
            attributes.addFlashAttribute("smsg","删除成功");

        }
        catch (ServiceException e){
            attributes.addFlashAttribute("wmsg","删除失败");
        }finally {
            return "redirect:/admin/users/"+page;
        }



    }


    //提交要搜索的数据
    @GetMapping("/admin/users/{page}/select/{spage}")
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
            return "redirect:/admin/users/"+page;
        }


        //获取翻页按钮和翻页组件
        Map<String, Object> selectUserByAnyOrPageInputMap = userService.selectUserByAny(selectTxt, selectKindValue, spage);

        //搜索结果为空处理
        if (selectUserByAnyOrPageInputMap==null){
            redirectAttributes.addFlashAttribute("smsg","搜索结果为空");
            return "redirect:/admin/users/"+page;
        }
        //搜索成功
        List<User> sUsers = (List<User>)selectUserByAnyOrPageInputMap.get("sUsers");
        PageInput pageInput=(PageInput)selectUserByAnyOrPageInputMap.get("pageInput");
        //记录搜索前页数，用于返回按钮
        model.addAttribute("bPage",page);






        //传送搜索出来的数据与及翻页组件
        model.addAttribute("sUsers",sUsers);
        model.addAttribute("pageInput",pageInput);
        model.addAttribute("selectKindValue",selectKindValue);
        model.addAttribute("selectTxt",selectTxt);



        HttpSession session = request.getSession(true);
        //删除原有的session
        session.removeAttribute("selectKindValue");
        session.removeAttribute("selectTxt");
        session.removeAttribute("suserListSize");
        //session保存搜索字段，以及本页数据数量（用于删除本页最后一条数据返回上一页）
        session.setAttribute("selectKindValue",selectKindValue);
        session.setAttribute("selectTxt",selectTxt);
        session.setAttribute("suserListSize",sUsers.size());

        return "users/user";
    }

    //跳转到搜索修改页
    @RequestMapping("/admin/users/{page}/select/{spage}/edit{id}")
    public String selectEditBookHtml(
            @PathVariable int page,
            @PathVariable int spage,
            @PathVariable String id,
            Model model){

        User editInfo = userService.getEditInfo(id);


        




        model.addAttribute("selectEditInfo",editInfo);

        model.addAttribute("selectReturnPage",spage);
        model.addAttribute("page",page);


        return "users/edit";
    }

    //搜索编辑提交
    @GetMapping("/admin/users/{page}/select/{spage}/edit/submit")
    public String selectEditSubmit(
            @PathVariable int page,
            @PathVariable int spage, User user,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        //获取搜索关键字的session
        String selectTxt=null;
        int selectKindValue;
        HttpSession session = request.getSession(false);
        selectTxt = (String)session.getAttribute("selectTxt");
        selectKindValue = (int) session.getAttribute("selectKindValue");

        //输入校验
        if (
                user.getName().equals("")||
                        user.getPhoneNumber().equals("")||
                        !reTool.phoneNumber(user.getPhoneNumber())){
            redirectAttributes.addFlashAttribute("msg","修改失败,请注意修改格式！");
            return "redirect:/admin/users/"+page+"/edit"+user.getId();
        }

        if (userService.upDateUser(user)<1){
            redirectAttributes.addFlashAttribute("msg","修改失败");
            return "redirect:/admin/users/"+page+"/edit"+user.getId();
        }

        redirectAttributes.addFlashAttribute("smsg","修改成功");

        return "redirect:/admin/users/"+page+"/select/"+spage+"?selectKindValue="+selectKindValue+"&selectTxt="+URLEncoder.encode(selectTxt,"utf-8");

    }

    //搜索删除
    @RequestMapping("/admin/users/{page}/select/{spage}/delete{id}/submit")
    public String selectDeleteSubmit(
            @PathVariable int page,
            @PathVariable int spage,
            @PathVariable String id,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) throws UnsupportedEncodingException {


        try {
            userService.deleteUserById(id);
            //如果删除的是该页最后一条数据，则返回上一页
            HttpSession session = request.getSession(false);
            int suserListSize = (int) session.getAttribute("suserListSize");
            if (suserListSize - 1 == 0 && spage > 1) {
                spage--;
            }
            redirectAttributes.addFlashAttribute("smsg", "删除成功");

        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("wmsg", "删除失败");

        } finally {
            //获取搜索页关键字session
            HttpSession session = request.getSession(false);
            String selectTxt = (String) session.getAttribute("selectTxt");
            int selectKindValue = (int) session.getAttribute("selectKindValue");

            return "redirect:/admin/users/" + page + "/select/" + spage + "?selectKindValue=" + selectKindValue + "&selectTxt=" + URLEncoder.encode(selectTxt, "utf-8");
        }


    }
}
