package com.gseek.gs.config;

import com.gseek.gs.config.login.handler.BlackListInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc设置.
 *
 * @author Phak
 * @since 2023/5/20-21:25
 */
@Component
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    BlackListInterceptor blackListInterceptor;

    /**
     * 注册自定义拦截器,指定要拦截的路径.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(blackListInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/users/login","/users/register","/users/account", "/users/account/**");
    }
}
