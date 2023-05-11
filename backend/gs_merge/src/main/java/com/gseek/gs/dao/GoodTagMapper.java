package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.TagDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-12:38
 */
@Mapper
public interface GoodTagMapper {
    //todo 补充注释
    List<Integer> selectGoodIdByTagId(@Param("tagId") int tagId);

    /**
     * 删除商品除typeId外的tagId，再添加tagId
     *
     *
     * */
    int updateGoodTag(@Param("goodId") int goodId,@Param("tags") List<TagDO> tags);

    List<String> selectTagNamesByGoodId(@Param("goodId") int goodId);

    String selectTypeTagNamesByGoodId(@Param("goodId") int goodId);
}
