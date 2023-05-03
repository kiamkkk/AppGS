package com.gseek.gseek0_0.service;

import com.gseek.gseek0_0.dao.UserMapper;
import com.gseek.gseek0_0.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
@Autowired
    private UserMapper userMapper;
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserPassword(){
        return userMapper.getUserPassword();}


}
