package com.gseek.gseek0_0.service;

import com.gseek.gseek0_0.pojo.UserPassword;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserPassword getUserPassword();
}
