package com.cai.library.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.expression.Lists;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
//    @Resource
//    EncodingFilter encodingFilter;
    @Override
    //视图解析器
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        WebMvcConfigurer.super.addViewControllers(registry);
    }

    @Override
    //拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);


        //所有请求的拦截器
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            //在业务处理器处理请求之前被调用
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String requestURL = request.getRequestURI();

                String regex="(/[a-z]+/\\d/[a-z]+)(/\\d){2}/";

                Pattern pattern= Pattern.compile(regex);
                Matcher matcher = pattern.matcher(requestURL);
                if (matcher.find()&&matcher.groupCount()==2){
                    String newUrl ="http://localhost:8080"+ matcher.group(1) + matcher.group(2)+"?"+request.getQueryString();
                    response.sendRedirect(newUrl);

                    return  false;

                }



//
//              return super.preHandle(request, response, handler);是返回父类的这个方法，如果自己自定义了请求或者返回，会执行两次就会报错
//                return super.preHandle(request, response, handler);
                return true;
            }

            @Override
            //在业务处理器处理请求完成之后，生成视图之前执行。后处理（调用了Service并返回ModelAndView，但没有渲染），可以对ModelAndView做更改
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                super.postHandle(request, response, handler, modelAndView);
            }

            @Override
            //在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                super.afterCompletion(request, response, handler, ex);
            }
        }).addPathPatterns("/**").
                excludePathPatterns("/index.html","/","/login","/css/*","/js/*","/img/*");

        //admin拦截器
       registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            //在业务处理器处理请求之前被调用
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


                //登录成功后，应该有用户的session
                //管理员
                String admin = (String) request.getSession().getAttribute("admin");



                if (admin == null || admin.equals("")) {

                    HttpSession session = request.getSession();
                    session.setAttribute("msg","没有权限，请重新登录");
                    response.sendRedirect("/");

                    return false;
                }


                //
                //              return super.preHandle(request, response, handler);是返回父类的这个方法，如果自己自定义了请求或者返回，会执行两次就会报错
                //                return super.preHandle(request, response, handler);
                return true;
            }

            @Override
            //在业务处理器处理请求完成之后，生成视图之前执行。后处理（调用了Service并返回ModelAndView，但没有渲染），可以对ModelAndView做更改
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                super.postHandle(request, response, handler, modelAndView);
            }

            @Override
            //在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                super.afterCompletion(request, response, handler, ex);
            }
        }).addPathPatterns("/admin/**").
               excludePathPatterns("/index.html","/","/login","/css/*","/js/*","/img/*");


        //user拦截器

        registry.addInterceptor(new HandlerInterceptorAdapter() {
                    @Override
                    //在业务处理器处理请求之前被调用
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {




                        //登录成功后，应该有用户的session
                        //用户
                        String userAccount = (String) request.getSession().getAttribute("account");

                        if (userAccount == null || userAccount.equals("")){
                            HttpSession session = request.getSession();
                            session.setAttribute("msg","没有权限，请重新登录");
                            response.sendRedirect("/");
                        }







//
//              return super.preHandle(request, response, handler);是返回父类的这个方法，如果自己自定义了请求或者返回，会执行两次就会报错
//                return super.preHandle(request, response, handler);
                        return true;
                    }

                    @Override
                    //在业务处理器处理请求完成之后，生成视图之前执行。后处理（调用了Service并返回ModelAndView，但没有渲染），可以对ModelAndView做更改
                    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                        super.postHandle(request, response, handler, modelAndView);
                    }

                    @Override
                    //在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
                    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                        super.afterCompletion(request, response, handler, ex);
                    }
                }).addPathPatterns("/user/**").
                excludePathPatterns("/index.html","/","/login","/css/*","/js/*","/img/*");

        //令牌拦截器
        registry.addInterceptor(buketInterceptor()).
                addPathPatterns("/**").excludePathPatterns("/index.html","/","/login","/css/*","/js/*","/img/*");
    }


    //请求和响应信息的编码转换器


//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        WebMvcConfigurer.super.configureMessageConverters(converters);
//        converters.add(responseBodyConverter());
//
//
//    }
//
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        WebMvcConfigurer.super.configureContentNegotiation(configurer);
//        configurer.favorPathExtension(false);
//
//    }

//    添加过滤器
//    @Bean
//    public FilterRegistrationBean testFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean(encodingFilter);
//        registration.addUrlPatterns("/*");
//        registration.setName("tencodingFilter");
//        registration.setOrder(1);
//        return registration;
//    }
//
//    @Bean
//    public HttpMessageConverter<String> responseBodyConverter() {
//        StringHttpMessageConverter converter = new StringHttpMessageConverter(
//                Charset.forName("UTF-8"));
//        return converter;
//    }

    @Bean
    //令牌拦截器
    public BuketInterceptor buketInterceptor(){
        return new BuketInterceptor();
    }



}
