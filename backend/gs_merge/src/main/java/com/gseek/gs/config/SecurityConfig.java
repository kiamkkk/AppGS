package com.gseek.gs.config;

import com.gseek.gs.config.login.handler.*;
import com.gseek.gs.config.login.handler.admin.*;
import com.gseek.gs.service.inter.AdminService;
import com.gseek.gs.service.inter.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.ForceEagerSessionCreationFilter;

/**
 * SpringSecurity 配置类
 *
 * @author Phak
 * @since 2023/5/2-19:37
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;
    @Autowired
    @Qualifier("adminServiceImpl")
    AdminService adminService;
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    /** update by Isabella at 2023/5/17-21:20 **/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   CustomAuthenticationSuccessHandler successHandler,
                                                   CustomAuthenticationFailureHandler failureHandler,
                                                   AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler,
                                                   AdminAuthenticationFailureHandler adminAuthenticationFailureHandler)
            throws Exception {
        httpSecurity.csrf().disable();

        httpSecurity.authorizeHttpRequests()
//                .anyRequest().permitAll()
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers("/alipay/**","/imgs/**","/users/register","/users").permitAll()
                .requestMatchers("/report/**","/report").permitAll()
                .requestMatchers("/after_sale/**").permitAll()
                .requestMatchers("/users/**","/buyer/**","/buyer/**","/goods/**","/seller/**","/trade/**").hasAnyAuthority("USER","ADMIN")
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
               /* .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)*/
        ;


        httpSecurity
                .addFilterAt(authenticationFilter(successHandler, failureHandler), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(adminAuthenticationFilter(adminAuthenticationSuccessHandler, adminAuthenticationFailureHandler), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, CustomAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, AdminAuthenticationFilter.class)
                .addFilterBefore((servletRequest, servletResponse, filterChain) -> {
                    ServletRequest requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest)servletRequest);
                    filterChain.doFilter(requestWrapper, servletResponse);//requestWrapper中保存着供二次使用的请求数据
                }, ForceEagerSessionCreationFilter.class)
                .addFilterBefore((servletRequest, servletResponse, filterChain) -> {
                    ServletRequest requestWrapper = new AdminHttpServletRequestWrapper((HttpServletRequest)servletRequest);
                    filterChain.doFilter(requestWrapper, servletResponse);//requestWrapper中保存着供二次使用的请求数据
                }, ForceEagerSessionCreationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        /*MessageDigestPasswordEncoder passwordEncoder=new MessageDigestPasswordEncoder("MD5");*/
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public CustomAuthenticationFilter authenticationFilter(CustomAuthenticationSuccessHandler successHandler,
                                                           CustomAuthenticationFailureHandler failureHandler) {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(new ProviderManager(authenticationProvider(userService())));
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationDetailsSource(new CustomWebAuthenticationDetailsSource());
        return filter;
    }
    @Bean
    public AdminAuthenticationFilter adminAuthenticationFilter(AdminAuthenticationSuccessHandler successHandler,
                                                               AdminAuthenticationFailureHandler failureHandler) {
        AdminAuthenticationFilter filter = new AdminAuthenticationFilter();
        filter.setAuthenticationManager(new ProviderManager(adminAuthenticationProvider(adminService())));
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationDetailsSource(new AdminWebAuthenticationDetailsSource());
        return filter;
    }

    @Bean
    public UserService userService(){
        return userService;
    }
    @Bean
    public AdminService adminService(){
        return adminService;
    }


    @Bean
    public CustomDaoAuthenticationProvider authenticationProvider(UserService userService) {
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }
    @Bean
    public AdminDaoAuthenticationProvider adminAuthenticationProvider(AdminService adminService) {
        AdminDaoAuthenticationProvider provider = new AdminDaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(adminService);
        return provider;
    }

}