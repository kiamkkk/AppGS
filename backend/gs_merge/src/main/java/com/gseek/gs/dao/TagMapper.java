package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.TagDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-13:01
 */
@Mapper
public interface TagMapper {
    //todo 补充注释

    /**
     * 查找,不使tag点击数加一
     *
     * @param tagText tag名称
     * @return tagId tag主键
     * */
    TagDO selectTagByTagName(@Param("tagText") String tagText);

    /**
     * 查找的同时使tag点击数加一
     *
     * @param tagText tag名称
     * @return tagId tag主键
     * */
    @Transactional(rollbackFor = Exception.class)
    TagDO selectTagByTagNameThenAdd(@Param("tagText") String tagText);

    /**
     * 主键回显
     *
     */
    int insertTag(@Param("tags") List<TagDO> tags);



}
