package com.cai.library.controller;

import com.cai.library.service.BookImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;

@Controller
public class BookImgController {
//  static String[] adminUrl={
//          "/admin/books/{page}/img",
//          "/admin/books/{page}/select/{spage}/img",
//          "/admin/BBI/{page}/img",
//          "/admin/BBI/{page}/select/{spage}/img",
//          "/admin/UBI/{page}/img",
//          "/admin/UBI/{page}/select/{spage}/img",
//          "/admin/users/{page}/img",
//          "/admin/users/{page}/select/{spage}/img"
//
//
//  };

  static String[] userUrl={

  };
  @Autowired
  BookImgService bookImgService;

    @RequestMapping({
            "/admin/books/{page}/img",
            "/admin/books/{page}/select/{spage}/img",
            "/admin/BBI/{page}/img",
            "/admin/BBI/{page}/select/{spage}/img",
            "/admin/UBI/{page}/img",
            "/admin/UBI/{page}/select/{spage}/img",
            "/admin/users/{page}/img",
            "/admin/users/{page}/select/{spage}/img",
            "/admin/hotBook/img"
    })
    public String adminBookImg(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "active") String active,
            Model model
    ) throws FileNotFoundException {
        //获取图片url
      String bookImg = bookImgService.getBookImg(id);
      switch (active){

      }

      model.addAttribute("img",bookImg);

      //高亮参数返回
      model.addAttribute("active",active);


      return "adminimg";
    }



  @RequestMapping({
          "/user/BAL/{page}/img",
          "/user/libraryBook{kindIndex}/{page}/img",
          "/user/libraryBook{kindIndex}/{page}/select/{spage}/img"



  })
  public String userBookImg(
          @RequestParam(name = "id") String id,
          @RequestParam(name = "active") String active,
          Model model
  ) throws FileNotFoundException {
    //获取图片url
    String bookImg = bookImgService.getBookImg(id);

    model.addAttribute("img",bookImg);

    //高亮参数返回
    model.addAttribute("active",active);

    return "userimg";
  }


}
