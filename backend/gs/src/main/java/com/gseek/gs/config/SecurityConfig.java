package com.gseek.gs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SpringSecurity 配置类
 *
 * @author Phak
 * @since 2023/5/2-19:37
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.cors().disable();
        httpSecurity.httpBasic().disable();

        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/users/register").anonymous();

        return httpSecurity.build();
    }

}
