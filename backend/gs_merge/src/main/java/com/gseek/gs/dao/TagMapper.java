package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.TagDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 操作tag表.
 *
 * @author Phak
 * @since 2023/5/9-13:01
 */
@Mapper
public interface TagMapper {

    /**
     * 批量插入tag信息.
     * 如果数据库中没有tag,会新建;已有则忽略
     * 主键回显.
     */
    int insertTags(@Param("tags") List<TagDO> tags);

    /**
     * 根据tag内容,获取tag信息.
     * */
    TagDO selectTagByTagName(@Param("tagText") String tagText);

    /**
     * 根据多个tag内容,获取tag信息.
     * */
    List<TagDO> selectTagsByTagNames(@Param("tags") List<String> tags);

    /**
     * 根据tag内容,获取tag信息的同时使tag点击数加一
     * */
    @Transactional(rollbackFor = Exception.class)
    TagDO selectTagByTagNameThenAdd(@Param("tagText") String tagText);
}
