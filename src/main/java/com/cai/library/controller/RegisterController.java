package com.cai.library.controller;

import com.cai.library.dao.admin.User;
import com.cai.library.dao.user.LoginUser;
import com.cai.library.exception.MYSqlException;
import com.cai.library.exception.SendVerificationCodeException;
import com.cai.library.pojo.MD5;
import com.cai.library.pojo.MailRequest;
import com.cai.library.pojo.VerificationCode;
import com.cai.library.pojo.VerificationTool;
import com.cai.library.service.EmailService;
import com.cai.library.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {
    @Autowired
    EmailService emailService;
    @Autowired
    RegisterService registerService;
    @Autowired
    VerificationTool verificationTool;
    @Autowired
    VerificationCode verificationCode;




    //跳转到注册页
    @RequestMapping("/register")
    public String registerHtml(
            @RequestParam(name = "type",required = false)Integer type,
            @RequestParam(name = "mail",required = false)String mail,
            @RequestParam(name = "emsg",required = false)String emsg,
            @RequestParam(name = "wmsg",required = false) String wmsg,
            @RequestParam(name = "verificationCode" ,required = false)String vCode,
            @RequestParam(name = "user",required = false) User rUser,
            @RequestParam(name = "loginUser",required = false) LoginUser rloginUser,

            User user,
            LoginUser loginUser,
            Model model
    ){

        //显示样式
        //0:显示验证码form,1:显示注册信息form
//        Integer type=0;
//        System.out.println(emsg);
//        System.out.println(type);
//
//        if (emsg!=null && emsg.equals("")){
//
//            model.addAttribute("emsg",emsg);
//            type=1;
//
//
//        }
//
//        if (wmsg!=null && wmsg.equals("")){
//            model.addAttribute("wmsg",wmsg);
//
//        }
//
//        if (mail!=null && !mail.equals("")){
//            model.addAttribute("mail",mail);
//            type=1;
//        }
//
//        if (vCode!=null&&!vCode.equals("")){
//            model.addAttribute("verificationCode",vCode);
//            type=1;
//        }
//
//        if (rloginUser!=null
//        ){
//            model.addAttribute("loginUser",rloginUser);
//            type=1;
//        }
//
//        if (rUser!=null
//        ){
//            model.addAttribute("user",rUser);
//            type=1;
//        }
//
//
//        model.addAttribute("type",type);



        return "register";
    }





    //发送验证码
    @PostMapping("/register/getVC")
    public String getverificationCode(
            @RequestParam(name = "mail",required = false)String mail,


            RedirectAttributes attributes
    ){

        if (mail==null&&mail.equals("")){
            attributes.addFlashAttribute("wmsg","邮箱为空");

        }
        try {
            emailService.sendVerificationCode(mail);

            attributes.addFlashAttribute("emsg","已发送验证码，1小时内有效");
            attributes.addFlashAttribute("mail",mail);
            attributes.addFlashAttribute("type",1);



        }catch (SendVerificationCodeException e){
            attributes.addFlashAttribute("wmsg","验证码未发送");


        }finally {
            return "redirect:/register";
        }








    }




    //提交注册信息
    @PostMapping ("/register/submit")
    public String registerSubmit(
            @RequestParam(name = "mail",required = false)String mail,
            @RequestParam(name = "verificationCode",required = false)String vCode,
            User user,
            LoginUser loginUser,
            RedirectAttributes attributes
    ){
        //验证输入是否正确


        if (

                vCode==null || vCode.equals("")||
                user.getName()==null || user.getName().equals("") ||
                user.getPhoneNumber()==null || user.getPhoneNumber().equals("") ||
                loginUser.getAccount()==null || loginUser.equals("") ||
                loginUser.getPassword()==null || loginUser.getPassword().equals("")
        ){

            attributes.addFlashAttribute("user",user);
            attributes.addFlashAttribute("loginUser",loginUser);
            attributes.addFlashAttribute("mail",mail);

            attributes.addFlashAttribute("verificationCode",vCode);
            attributes.addFlashAttribute("wmsg","存在空输入");
            attributes.addFlashAttribute("type",1);
            return "redirect:/register";

        }


        //验证验证码是否被删除
        if (!(MailRequest.mailTimeMap.containsKey(mail) &&
                MailRequest.verificationCodeMap.containsKey(mail))) {



            attributes.addFlashAttribute("mail",mail);
            attributes.addFlashAttribute("type",0);

            attributes.addFlashAttribute("wmsg","验证码超时或者未发送");
            return "redirect:/register";
        }


        //验证验证码是否超时
        if (verificationCode.getVerificationCodeOvertime(mail,vCode)>3600000){
            //删除超时验证码
            verificationCode.clearVerificationCode(mail);
            attributes.addFlashAttribute("mail",mail);
            attributes.addFlashAttribute("type",0);
            attributes.addFlashAttribute("wmsg","验证码超时");
            return "redirect:/register";
        }

        //验证验证码是否正确

        if (!MailRequest.verificationCodeMap.get(mail).equals(vCode)){
            attributes.addFlashAttribute("user",user);
            attributes.addFlashAttribute("loginUser",loginUser);
            attributes.addFlashAttribute("verificationCode",vCode);
            attributes.addFlashAttribute("mail",mail);
            attributes.addFlashAttribute("type",1);
            attributes.addFlashAttribute("wmsg","验证码错误");
            return "redirect:/register";
        }
        //验证账号是否存在
        if (verificationTool.userAccountVerification(loginUser.getAccount())) {
            attributes.addFlashAttribute("user",user);
            attributes.addFlashAttribute("loginUser",loginUser);
            attributes.addFlashAttribute("verificationCode",vCode);
            attributes.addFlashAttribute("mail",mail);
            attributes.addFlashAttribute("type",1);
            attributes.addFlashAttribute("wmsg","账号名已经存在");

            return "redirect:/register";
        }

        try {
            attributes.addFlashAttribute("msg","注册成功");
            registerService.addRegister(user,loginUser);

        }catch (MYSqlException e){
            attributes.addFlashAttribute("msg","注册失败");
        }finally {
            return "redirect:/";
        }







    }

}
