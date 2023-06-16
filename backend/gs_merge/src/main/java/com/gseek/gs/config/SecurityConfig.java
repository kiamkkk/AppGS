package com.gseek.gs.config;

import com.gseek.gs.config.login.handler.*;
import com.gseek.gs.config.login.handler.admin.*;
import com.gseek.gs.service.inter.AdminService;
import com.gseek.gs.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity 配置类
 *
 * @author Phak
 * @since 2023/5/2-19:37
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private String localhost;
    private String port;
    private String contextPath;
    public String baseUrl;
    public String loginFormUrl;

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;
    @Autowired
    @Qualifier("adminServiceImpl")
    AdminService adminService;
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    CustomLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    CustomLogoutHandler logoutHandler;


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
                // 路径拦截
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers(
                        "/alipay/**","/imgs/**","/users/register","/users",
                        "/error","/error/**"
                ).permitAll()
                .requestMatchers("/report/**","/report").permitAll()
                .requestMatchers("/after_sale/**").permitAll()
                .requestMatchers(
                        "/users/**","/buyer/**","/buyer/**",
                        "/goods/**","/seller/**","/trade/**","/goods"
                ).hasAnyAuthority("USER","ADMIN")
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()

                .and().exceptionHandling().authenticationEntryPoint(
                        customerAuthenticationEntryPoint()
                );

        httpSecurity
                .addFilterAt(
                        authenticationFilter(
                                successHandler,
                                failureHandler
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAt(
                        adminAuthenticationFilter(
                                adminAuthenticationSuccessHandler,
                                adminAuthenticationFailureHandler
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(jwtAuthenticationTokenFilter, ExceptionTranslationFilter.class)
/*                .addFilterBefore(jwtAuthenticationTokenFilter, AdminAuthenticationFilter.class)*/
                ;

        httpSecurity
                .logout()
                .logoutUrl("/users/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomerMD5PasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter authenticationFilter(CustomAuthenticationSuccessHandler successHandler,
                                                           CustomAuthenticationFailureHandler failureHandler) {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();

        filter.setAuthenticationManager(
                new ProviderManager(
                        userAuthenticationProvider(userService()
                        )
                )
        );

        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationDetailsSource(new CustomWebAuthenticationDetailsSource());
        return filter;
    }
    @Bean
    public AdminAuthenticationFilter adminAuthenticationFilter(AdminAuthenticationSuccessHandler successHandler,
                                                               AdminAuthenticationFailureHandler failureHandler) {
        AdminAuthenticationFilter filter = new AdminAuthenticationFilter();
        filter.setAuthenticationManager(
                new ProviderManager(
                        adminAuthenticationProvider(adminService()
                        )
                )
        );
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
    public CustomDaoAuthenticationProvider userAuthenticationProvider(UserService userService) {
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }
    @Bean
    public AdminDaoAuthenticationProvider adminAuthenticationProvider(AdminService adminService) {
        AdminDaoAuthenticationProvider provider = new AdminDaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(adminService);
        return provider;
    }

    @Bean
    public CustomerAuthenticationEntryPoint customerAuthenticationEntryPoint(){
        CustomerAuthenticationEntryPoint caep=new CustomerAuthenticationEntryPoint(getLoginFormUrl());
        return caep;
    }

    public String getLocalhost() {
        return localhost;
    }
    @Value("${custom.localhost}")
    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }
    public String getPort() {
        return port;
    }
    @Value("${server.port}")
    public void setPort(String port) {
        this.port = port;
    }
    public String getContextPath() {
        return contextPath;
    }
    @Value("${server.servlet.context-path}")
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
    private String getBaseUrl() {
        if (baseUrl==null){
            baseUrl="http://"+getLocalhost()+":"+getPort()+getContextPath();
        }
        return baseUrl;
    }
    private String getLoginFormUrl() {
        if (loginFormUrl==null){
            loginFormUrl=getBaseUrl()+"users";
        }
        return loginFormUrl;
    }
}