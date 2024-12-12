package com.fileManager.filter;

import com.fileManager.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws java.io.IOException, javax.servlet.ServletException {

        String authHeader = request.getHeader("Authorization");

        // 检查 Authorization 头是否存在并且以 "Bearer " 开头
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 提取 Token
            if (jwtUtil.validateToken(token)) { // 验证 Token 的合法性
                String username = jwtUtil.getUsernameFromToken(token);

                // 创建 UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 设置到 Spring Security 上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 继续处理请求
        chain.doFilter(request, response);
    }
}
