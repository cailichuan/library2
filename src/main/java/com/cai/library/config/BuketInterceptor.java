package com.cai.library.config;

import com.cai.library.anntation.CurrentLimiting;
import com.cai.library.pojo.BucketUtil;
import com.cai.library.exception.CurrentLimitingException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/*
*令牌拦截器
*/
public class BuketInterceptor implements HandlerInterceptor {

    // 预处理回调方法，在接口调用之前使用  true代表放行  false代表不放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        //获取方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //获取方法的CurrentLimiting注解
        CurrentLimiting annotation = method.getAnnotation(CurrentLimiting.class);
        if (annotation!=null){
            //根据令牌桶名取令牌，取到放行，没取到则抛出异常
            if (BucketUtil.buckets.get(annotation.bucketName()).getToken()){
                return true;
            }
            else
                throw new CurrentLimitingException("不好意识您被限流了");
        }
        else
            return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
