package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.dao.*;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.pojo.business.GoodsWithoutAccountAndSoldBO;
import com.gseek.gs.pojo.data.GoodDO;
import com.gseek.gs.service.inter.GoodService;
import lombok.extern.slf4j.Slf4j;
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

    @Autowired
    GoodCoverPicMapper goodCoverPicMapper;
    @Autowired
    GoodDetailPicMapper goodDetailPicMapper;
    @Override
    public String getGoodsByType(String typeName) throws JsonProcessingException {
        return getGoodsByTag(typeName);
    }

    @Override
    public String getGoodsByTag(String tagName) throws JsonProcessingException {

        //根据tagName查询goodId,同时tag的点击数加一
        int tagId=tagMapper.selectTagByTagNameThenAdd(tagName).getTagId();
        List<Integer> goodIds=goodTagMapper.selectGoodIdByTagId(tagId);
        //根据goodId获取商品并修改
        List<GoodsWithoutAccountAndSoldBO> goods=new ArrayList<>();
        for (int id:goodIds){
            GoodDO goodDO =goodMapper.selectGoodByGoodIdFully(id);
            List<String> coverPics=goodCoverPicMapper.selectCoversByGoodId(id);
            List<String> detailPics=goodDetailPicMapper.selectDetailsByGoodId(id);
            GoodsWithoutAccountAndSoldBO bo=new GoodsWithoutAccountAndSoldBO(goodDO,coverPics,detailPics);
            goods.add(bo);
        }

        return objectMapper.writeValueAsString(goods);
    }

    @Override
    public String getGoodByGoodId(int goodId) throws JsonProcessingException {
        //入参检验
        if (goodId==0){
            throw new ParameterWrongException(
                    new ParameterWrongBean()
                            .addParameters("goodId",goodId+"")
            );
        }

        GoodDO goodDO=goodMapper.selectGoodByGoodIdFully(goodId);
        List<String> covers=goodCoverPicMapper.selectCoversByGoodId(goodId);
        List<String> details=goodDetailPicMapper.selectDetailsByGoodId(goodId);
        GoodsWithoutAccountAndSoldBO bo=new GoodsWithoutAccountAndSoldBO(goodDO,covers,details);

        return objectMapper.writeValueAsString(bo);
    }
}
