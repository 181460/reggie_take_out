package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Description:
 * @author: jjw
 * @create: 2022-04-28-22:14
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFIlter implements Filter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();


        String[] str = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        boolean check = check(str, requestURI);
        if(check){
            filterChain.doFilter(request, response);
            return;
        }
        if(request.getSession().getAttribute("employee") != null){
            Long id = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute("user") != null) {
            Long userid = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userid) ;
            filterChain.doFilter(request, response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    public boolean check(String[] str,String requestURI){
        for(String url: str){
            if(PATH_MATCHER.match(url,requestURI)){
                return true;
            }
        }
        return false;
    }
}
