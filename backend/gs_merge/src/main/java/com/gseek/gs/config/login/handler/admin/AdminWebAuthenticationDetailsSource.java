package com.gseek.gs.config.login.handler.admin;

import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 * @author Isabella
 * @since 2023/5/17-14:36
 */
public class AdminWebAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

    @Override
    public AdminWebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new AdminWebAuthenticationDetails(context);
    }

}
