package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.TagDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作good_tag表.
 *
 * @author Phak
 * @since 2023/5/9-12:38
 */
@Mapper
public interface GoodTagMapper {

    /**
     * 删除商品除type_tag_id外的tagId，再添加tagId
     * */
    int updateGoodTag(@Param("goodId") int goodId,@Param("tags") List<TagDO> tags);

    /**
     * 根据商品id,删除商品与全部对应tag的关系.
     * 物理删除.
     * */
    int deleteAllGoodTagsByGoodId(@Param("goodId") int goodId);

    /**
     * 根据商品id,获取对应的tag名.
     * */
    List<String> selectTagNamesByGoodId(@Param("goodId") int goodId);

    /**
     * 根据tagId,获取对应的全部商品id
     * */
    List<Integer> selectGoodIdByTagId(@Param("tagId") int tagId);

    /**
     * 根据商品id,获取类型tagId(type_tag_id)
     * */
    String selectTypeTagNamesByGoodId(@Param("goodId") int goodId);
}
