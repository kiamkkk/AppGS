package com.gseek.gs.dao;

import com.gseek.gs.pojo.UserInformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Phak
 * @since 2023/5/2-19:56
 */

@Mapper
public interface UserInformationMapper {

    /**
     * 插入新一般用户的一般个人信息.
     * 注册用户的第二个步骤.
     *
     * @param userInformation 包含: userId 用户id
     *                             email 邮箱
     *                             profile_photo 头像url地址
     *
     * @return row 受影响行数
     * */
    int insertUserInformation(@Param("userInformation") UserInformation userInformation);


}
