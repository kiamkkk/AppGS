package com.gseek.gs.config.login.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 * @author Phak
 * @since 2023/5/5-11:46
 */
public class CustomWebAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

    @Override
    public CustomWebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new CustomWebAuthenticationDetails(context);
    }
}
