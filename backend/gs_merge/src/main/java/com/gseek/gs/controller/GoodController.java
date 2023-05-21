package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.service.inter.GoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Phak
 * @since 2023/5/9-12:36
 */
@Controller
@Slf4j
@RequestMapping("/goods")
public class GoodController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired()
    @Qualifier("goodServiceImpl")
    GoodService goodService;


    @GetMapping("/{type}")
    public String getGoodsByType(@PathVariable("type") String typeName)
            throws JsonProcessingException, ParameterWrongException {
        //入参检验
        if (typeName==null){
            throw new ParameterWrongException(
                    new ParameterWrongBean()
                            .addParameters("type",typeName+"")
            );
        }
        return goodService.getGoodsByType(typeName);
    }

    @GetMapping("/{tag}")
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

    @GetMapping("{good_id}")
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

}
