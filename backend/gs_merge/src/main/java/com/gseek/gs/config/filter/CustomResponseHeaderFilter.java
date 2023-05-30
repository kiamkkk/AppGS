package com.gseek.gs.config.filter;

import com.google.common.base.Objects;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 将get以外的请求方法的响应状态码改为201.
 *
 * @author Phak
 * @since 2023/5/27-23:09
 */
@WebFilter(urlPatterns="/*")
public class CustomResponseHeaderFilter extends OncePerRequestFilter {

    /**
     * 根据request的请求类型决定响应码是200还是201.
     * */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        // 只对正常响应进行操作
        if (response.getStatus() == 200){
            // 请求方法不为get则为post、patch、put、delete，统一设置为201
            // 不确定OPTIONS会不会被影响，先写在这好了
            String method=request.getMethod();
            if (! Objects.equal(method, "GET") || ! Objects.equal(method, "OPTIONS")){
                response.setStatus(201);
            }
        }

        filterChain.doFilter(request, response);
    }
}
