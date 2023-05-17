package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.AdminDO;
import com.gseek.gs.pojo.data.UserPasswordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: Isabella
 * @create: 2023-05-17 16:50
 **/
@Mapper
public interface AdminMapper {
    //TODO 要不要能添加
    int insertAdmin(AdminDO adminDO);

    AdminDO selectAdminByAdminName( String adminName);

    int updateAdmin(AdminDO adminDO);
    String selectAdminNameByAdminId( int adminId);
}
