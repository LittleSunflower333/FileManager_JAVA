package com.fileManager.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
@Order(2)  // 设置为较晚执行，确保 CORS 先处理
public class SameSiteFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("SameSite过滤器生效");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Cookie[] cookies = httpRequest.getCookies();
       Boolean isSafe = "true".equals(httpRequest.getHeader("x-is-safe"));
        if(isSafe){//安全状态下设置SameSite属性
            httpResponse.setHeader("Set-Cookie", "key=value; SameSite=Strict");
        }
        chain.doFilter(request, response);
    }
}
