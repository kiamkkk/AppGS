package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.UserIdentificationBO;
import com.gseek.gs.pojo.data.UserIdentificationDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 操作user_identification表.
 *
 * @author Phak
 * @since 2023/5/2-20:09
 */
@Mapper
public interface UserIdentificationMapper {

    /**
     * 插入新一般用户的实名认证信息.
     * 注册用户的第三个步骤.
     * */
    int insertUserIdentification(@Param("userIdentificationDO") UserIdentificationDO userIdentificationDO);

    /**
     * 根据用户id,更新用户实名认证信息.
     * */
    int updateUserIdentification(@Param("userIdentificationDO") UserIdentificationDO userIdentificationDO);

    /**
     * 根据用户id查询用户实名认证信息.
     * */
    UserIdentificationBO selectUserIdentificationByUserId(@Param("userId") int userId);

}
