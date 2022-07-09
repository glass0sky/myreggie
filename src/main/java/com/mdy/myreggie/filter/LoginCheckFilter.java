package com.mdy.myreggie.filter;

import com.alibaba.fastjson.JSON;
import com.mdy.myreggie.common.BaseContext;
import com.mdy.myreggie.common.R;
import com.mdy.myreggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@WebFilter(filterName = "loginCheckFilter" ,urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final   AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        log.info("拦截到本次请求：{}",requestURI);
        String urls [] = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",  //短信发送端口
                "/user/login"    //移动端登陆
        };
        //不需要处理的请求直接放行
        boolean check = check(urls, requestURI);
        if (check){
            log.info("本次请求不需要处理....");
            filterChain.doFilter(request,response);
            return;
        }
        Long empId = (Long) request.getSession().getAttribute("employee");
        if (empId!=null){
            log.info("员工已经登陆成功，员工id： {}",empId);
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;

        }
        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId!=null){
            log.info("员工已经登陆成功，用户id： {}",userId);
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登陆...");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    public boolean check(String[] urls , String requestURI){
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
