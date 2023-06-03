package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.UserInformationBO;
import com.gseek.gs.pojo.data.UserInformationDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 操作user_information表.
 *
 * @author Phak
 * @since 2023/5/2-19:56
 */

@Mapper
public interface UserInformationMapper {

    /**
     * 插入新一般用户的一般个人信息.
     * 注册用户的第二个步骤.
     * */
    int insertUserInformation(@Param("userInformationDO") UserInformationDO userInformationDO);

    /**
     * 更新用户一般信息.
     * */
    int updateUserInformation(@Param("userInformationDO") UserInformationDO userInformationDO);

    /**
     * 根据用户id,获取用户一般信息.
     * */
    UserInformationBO selectUserInformationByUserId(@Param("userId") int userId);


}
