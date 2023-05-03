package com.gseek.gseek0_0.controller;

import com.gseek.gseek0_0.pojo.User;
import com.gseek.gseek0_0.service.UserService;
import com.gseek.gseek0_0.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired

    UserService userService;
    @RequestMapping("/get")
    public User getUser(){
        User user=userService.getUserPassword();
        return user;
    }
}
