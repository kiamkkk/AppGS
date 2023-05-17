package com.gseek.gs.controller;

import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Phak
 * @since 2023/5/3-22:23
 */
@Controller
@Slf4j
public class TestController {

    @GetMapping("index")
    public String index(){
        return "index";
    }

    @Autowired
    public SimpMessagingTemplate template;

    @RequestMapping("/pri")
    public String pri(){
        return "privateExample";
    }

    @RequestMapping("/pub")
    public String pub(){
        return "publicExample";
    }


    @GetMapping("/test")
    @ResponseBody
    public String test(@CurrentSecurityContext(expression = "authentication") Authentication authentication){
        if(authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            return details.getUserId()+"|"+authentication.getName()+"|"+authentication.getCredentials();
        }else {
            log.info("匿名用户访问");
            return "匿名用户访问"+"|"+authentication.getName()+"|"+authentication.getCredentials();
        }
    }

}
