package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.service.inter.GoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理商品信息获取.
 *
 * @author Phak
 * @since 2023/5/9-12:36
 */
@RestController
@Slf4j
@RequestMapping("/goods")
public class GoodController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("goodServiceImpl")
    GoodService goodService;

    /**
     * 根据商品种类获取商品信息.
     * */
    @GetMapping("/type/{type}")
    public String getGoodsByType(@PathVariable("type") String typeName)
            throws JsonProcessingException, ParameterWrongException {
        //入参检验
        if (typeName==null || typeName.isBlank()){
            throw new ParameterWrongException(
                    new ParameterWrongBean()
                            .addParameters("type",typeName+"")
            );
        }
        return goodService.getGoodsByType(typeName);
    }
    /**
     * 根据商品tag获取商品信息.
     * */
    @GetMapping("/tag/{tag}")
    public String getGoodsByTag(@PathVariable("tag") String tagName)
            throws JsonProcessingException, ParameterWrongException {
        if (tagName==null){
            throw new ParameterWrongException(
                    new ParameterWrongBean()
                            .addParameters("type",tagName+"")
            );
        }
        return goodService.getGoodsByTag(tagName);
    }

    /**
     * 根据商品id获取商品信息.
     * */
    @GetMapping("/good_id/{good_id}")
    public String getGoodByGoodId(@PathVariable("good_id") int goodId)
            throws JsonProcessingException, ParameterWrongException {
        if (goodId<=0){
            throw new ParameterWrongException(
                    new ParameterWrongBean()
                            .addParameters("type",goodId+"")
            );
        }
        return goodService.getGoodByGoodId(goodId);
    }

    /**
     * 获取所有通过审核的商品
     * */
    @GetMapping
    public String getAllCheckedGoods() throws JsonProcessingException {
        return goodService.queryAllCheckedGood();
    }
    /**
     * 获取所有名字内含有关键词的商品
     * */
    @GetMapping("/good_name/{good_name}")
    public String getGoodByGoodName(@PathVariable("good_name")String goodName) throws JsonProcessingException {
        return goodService.queryGoodByName(goodName);
    }

}
