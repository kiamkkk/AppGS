package com.gseek.gs.controller;

import com.gseek.gs.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Phak
 * @since 2023/5/2-19:00
 */

@RestController("/user")
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;


}
