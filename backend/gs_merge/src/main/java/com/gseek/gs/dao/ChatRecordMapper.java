package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.ChatDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作chat表.
 *
 * @author Phak
 * @since 2023/5/17-9:50
 */
@Mapper
public interface ChatRecordMapper {

    /**
     * 批量插入多条聊天记录.
     * */
    int insertChat(@Param("chatDOs") List<ChatDO> chatDO);
    /**
     * 根据商品id、用户id,获取聊天记录.
     * 用户id可以是发送者(from_user_id)或接收者(to_user_id)
     * */
    List<ChatDO> selectChat(@Param("goodId") int goodId, @Param("userId") int userId);
    /**
     * 根据商品id、用户id,获取聊天记录.
     * 用户id只能是接收者(to_user_id)
     * */
    int[] selectToUser(@Param("goodId") int goodId, @Param("userId") int userId);
}
