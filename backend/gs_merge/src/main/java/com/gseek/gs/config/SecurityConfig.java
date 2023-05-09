package com.gseek.gs.config;

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
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   CustomAuthenticationSuccessHandler successHandler,
                                                   CustomAuthenticationFailureHandler failureHandler)
            throws Exception {
        httpSecurity.csrf().disable();

        httpSecurity.authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers("/users/register","/users").permitAll()
                .requestMatchers("/users/**").hasAnyAuthority("USER","ADMIN")
                .anyRequest().authenticated()
               /* .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)*/
        ;


        httpSecurity
                .addFilterAt(authenticationFilter(successHandler, failureHandler), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, CustomAuthenticationFilter.class)
                .addFilterBefore((servletRequest, servletResponse, filterChain) -> {
                    ServletRequest requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest)servletRequest);
                    filterChain.doFilter(requestWrapper, servletResponse);//requestWrapper中保存着供二次使用的请求数据
                }, ForceEagerSessionCreationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //todo 继续用MD5？
        /*MessageDigestPasswordEncoder passwordEncoder=new MessageDigestPasswordEncoder("SHA-256");*/

        /*return new BCryptPasswordEncoder();*/
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
    public UserService userService(){
        return userService;
    }

    @Bean
    public CustomDaoAuthenticationProvider authenticationProvider(UserService userService) {
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

}