package com.gseek.gseek0_0.dao;

import com.gseek.gseek0_0.pojo.User;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
     User getUserPassword();
}
