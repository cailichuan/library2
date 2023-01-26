package com.cai.library.controller;

import com.cai.library.anntation.CurrentLimiting;
import com.cai.library.dao.user.LoginUser;
import com.cai.library.pojo.VerificationTool;
import com.cai.library.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller

public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    VerificationTool verificationTool;
//首页
    @RequestMapping("/")
    public String index(
            HttpSession session,
            Model model
    ){
        if (session.getAttribute("msg")!=null){
            String msg=(String) session.getAttribute("msg");

            if (msg!=null || !msg.equals("")){
                model.addAttribute("msg",msg);
                session.removeAttribute("msg");
            }
        }


        return "index";
    }



    //管理员登录成功
    @CurrentLimiting(bucketName = "admin",maxCount = 1,createRate = 1)
    @RequestMapping("/admin")
    public String AdminLoginSuccess(){

        return "borrow";
    }

    //用户登录成功
    @CurrentLimiting(bucketName = "user",maxCount = 1,createRate = 2)
    @RequestMapping("/user")
    public String userLoginSuccess(
            @ModelAttribute(name = "account") String account,
            HttpSession session,
            Model model
    ){
        if (!account.equals("")){
            model.addAttribute("account",account);

        }




        return "userhtml/user";
    }


    @RequestMapping("/test")
    @ResponseBody
    @CurrentLimiting(bucketName = "test",maxCount = 1,createRate = 1)
    public String totest(){
        return "test";
    }

    @PostMapping("/login")
    public String login(@RequestBody String user,
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        Model model) throws Exception {

        String[] splitstr = user.split("&");
        String username = splitstr[0].split("=")[1];
        String password = splitstr[1].split("=")[1];


        //确定是否是管理员
        if (verificationTool.adminVerification(username)){
            //验证账户密码是否正确
            if(loginService.adminLogin(username,password)){

                //登录成功
                session.setAttribute("admin",username);


                return "redirect:/admin";

            }else {
                redirectAttributes.addFlashAttribute("msg","用户名或者密码错误");
                return "redirect:/";

        }


        }else {
            //非管理员
            LoginUser loginUser = loginService.userLogin(username, password);

            //登录成功
            if (loginUser!=null){

                System.out.println(loginUser);
                redirectAttributes.addFlashAttribute("account",loginUser.getAccount());

                session.setAttribute("userId",loginUser.getUserId());
                session.setAttribute("account",loginUser.getAccount());



                return "redirect:/user";
            } else {
               redirectAttributes.addFlashAttribute("msg","用户名或者密码错误");
                return "redirect:/";
            }

        }





    }

    //用户注销
    @RequestMapping("/user/loginOut")
    public String userLoginOut(
            HttpSession session
    ){

        session.removeAttribute("userId");
        session.removeAttribute("account");


        return "redirect:/";
    }

    //管理员注销
    @RequestMapping("/admin/loginOut")
    public  String adminLoginOut(
            HttpSession session
    ){
        session.removeAttribute("admin");
        return "redirect:/";

    }

}
