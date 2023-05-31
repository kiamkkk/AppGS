package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.GoodTagMapper;
import com.gseek.gs.dao.TagMapper;
import com.gseek.gs.pojo.business.GoodsWithoutAccountAndSoldBO;
import com.gseek.gs.service.inter.GoodService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-13:20
 */
@Service("goodServiceImpl")
@Slf4j
public class GoodServiceImpl implements GoodService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    GoodTagMapper goodTagMapper;

    @Override
    public String getGoodsByType(String typeName) throws JsonProcessingException {
        return getGoodsByTag(typeName);
    }

    @Override
    public String getGoodsByTag(String tagName) throws JsonProcessingException {

        //根据tagName查询goodId,同时tag的点击数加一
        int tagId=tagMapper.selectTagByTagNameThenAdd(tagName).getTagId();
        List<Integer> goodIds=goodTagMapper.selectGoodIdByTagId(tagId);
        //根据goodId获取商品
        List<GoodsWithoutAccountAndSoldBO> goods= goodMapper.selectGoodsByGoodIdsWithoutAccountAndSold(goodIds);

        return objectMapper.writeValueAsString(goods);
    }

    @Override
    public String getGoodByGoodId(int goodId) throws JsonProcessingException {

        List<Integer> goodIds=new ArrayList<>();
        goodIds.add(goodId);
        List<GoodsWithoutAccountAndSoldBO> bos= goodMapper.selectGoodsByGoodIdsWithoutAccountAndSold(goodIds);

        if (bos.get(0)==null){
            return objectMapper.writeValueAsString("");
        }

        return objectMapper.writeValueAsString(bos.get(0));
    }
    @Override
    public String queryAllCheckedGood() throws JsonProcessingException {

        List<GoodsWithoutAccountAndSoldBO> list=goodMapper.queryAllCheckedGood();

        return objectMapper.writeValueAsString(list);
    }
    @Override
    public String queryGoodByName(@Param("goodName") String goodName) throws JsonProcessingException {
        goodName="%"+goodName+"%";
        List<GoodsWithoutAccountAndSoldBO> list=goodMapper.queryGoodByName(goodName);
        return objectMapper.writeValueAsString(list);
    }
}
