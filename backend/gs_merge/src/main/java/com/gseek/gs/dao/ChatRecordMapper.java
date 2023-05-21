package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.ChatDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/17-9:50
 */
@Mapper
public interface ChatRecordMapper {

    List<ChatDO> selectChat(@Param("goodId") int goodId, @Param("userId") int userId);

    int insertChat(@Param("chatDOs") List<ChatDO> chatDO);
}
