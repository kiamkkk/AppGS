package com.gseek.gseek0_0.service;

import com.gseek.gseek0_0.dao.UserMapper;
import com.gseek.gseek0_0.pojo.UserPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserPassword getUserPassword(){
        return userMapper.getUserPassword();}


}
