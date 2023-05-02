package com.gseek.gs.dao;

import com.gseek.gs.pojo.UserPassword;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Phak
 * @since 2023/5/2-19:33
 */

@Mapper
public interface UserPasswordMapper {

    /**
     * 插入新一般用户的密码信息.
     * 注册用户的第一个步骤,应该回显主键
     *
     * @param userPassword 包含: password 密码
     *                          salt 密匙盐
     *
     * @return row 受影响行数
     * */
    int insertUserPassword(@Param("userPassword") UserPassword userPassword);


}
