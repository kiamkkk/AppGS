package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Phak
 * @since 2023/5/9-13:20
 */
public interface GoodService {
//todo 补齐注释！！！
    /**
     *
     * @param typeName type名称
     * @return
     * */
    String getGoodsByType(String typeName) throws JsonProcessingException;

    /**
     *
     * @param tagName tag名称
     * @return
     * */
    String getGoodsByTag(@PathVariable("tag") String tagName) throws JsonProcessingException;

    /**
     *
     * @param goodId 商品id
     * @return
     * */
    String getGoodByGoodId(@PathVariable("good_id") int goodId) throws JsonProcessingException;

}
