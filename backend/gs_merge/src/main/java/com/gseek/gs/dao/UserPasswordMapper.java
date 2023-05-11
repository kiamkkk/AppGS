package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.UserPasswordDO;
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
     * @param userPasswordDO 包含: password 密码
     *                          salt 密匙盐
     *
     * @return row 受影响行数
     * */
    int insertUserPassword(@Param("userPasswordDO") UserPasswordDO userPasswordDO);

    /**
     * 根据用户名查找登录信息
     *
     * @param userName 用户名
     * @return 登录信息
     * */
    UserPasswordDO selectUserPasswordByUsername(@Param("userName") String userName);

    int updateUserPassword(@Param("userPasswordDO") UserPasswordDO userPasswordDO);

    /**
     *
     * */
    String selectUserNameByUserId(@Param("userId") int userId);

}
