package com.gseek.gs.config;

import com.gseek.gs.config.login.handler.CustomAuthenticationFailureHandler;
import com.gseek.gs.config.login.handler.CustomAuthenticationFilter;
import com.gseek.gs.config.login.handler.CustomAuthenticationSuccessHandler;
import com.gseek.gs.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    //todo 改造验证流程
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   CustomAuthenticationSuccessHandler successHandler,
                                                   CustomAuthenticationFailureHandler failureHandler)
            throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.httpBasic().disable();
        httpSecurity.formLogin().disable();

        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/users/register", "/users").anonymous()
                .anyRequest().hasAnyAuthority("USER", "ADMIN");

        httpSecurity.addFilterAt(authenticationFilter(successHandler, failureHandler),
                UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //todo 继续用MD5？
        /*MessageDigestPasswordEncoder passwordEncoder=new MessageDigestPasswordEncoder("SHA-256");*/

        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter authenticationFilter(CustomAuthenticationSuccessHandler successHandler,
                                                           CustomAuthenticationFailureHandler failureHandler) {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(new ProviderManager(authenticationProvider(userService())));
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    public UserService userService(){
        return userService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

}