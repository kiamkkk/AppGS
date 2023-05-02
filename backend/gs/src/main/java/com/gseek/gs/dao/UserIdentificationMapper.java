package com.gseek.gs.dao;

import com.gseek.gs.pojo.UserIdentification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Phak
 * @since 2023/5/2-20:09
 */
@Mapper
public interface UserIdentificationMapper {

    /**
     * 插入新一般用户的实名认证信息.
     * 注册用户的第三个步骤.
     *
     * @param userIdentification 包含: userId 用户id
     *                             idNumber 身份证号
     *                             certified 是否认证过
     *                             modifiedTime 认证时间
     *
     * @return row 受影响行数
     * */
    int insertUserIdentification(@Param("userIdentification") UserIdentification userIdentification);

}
