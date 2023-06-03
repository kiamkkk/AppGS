package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.UserPasswordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 操作user_password表.
 *
 * @author Phak
 * @since 2023/5/2-19:33
 */

@Mapper
public interface UserPasswordMapper {

    /**
     * 插入新一般用户的密码信息.
     * 注册用户的第一个步骤.
     * 主键回显.
     * */
    int insertUserPassword(@Param("userPasswordDO") UserPasswordDO userPasswordDO);

    /**
     * 选择性更新用户登录信息.
     * */
    int updateUserPasswordSelect(@Param("userPasswordDO") UserPasswordDO userPasswordDO);

    /**
     * 根据用户名,获取登录信息.
     * */
    UserPasswordDO selectUserPasswordByUsername(@Param("userName") String userName);

    /**
     * 根据用户id,获取用户名.
     * */
    String selectUserNameByUserId(@Param("userId") int userId);

    /**
     * 根据用户名,获取用户id.
     * */
    Integer selectUserIdByUserName(@Param("userName") String userName);

}
