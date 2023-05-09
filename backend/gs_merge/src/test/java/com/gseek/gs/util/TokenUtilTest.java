package com.gseek.gs.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-10:44
 */
@SpringBootTest
@Slf4j
public class TokenUtilTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void test(){
        List<Integer> integers= new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        ObjectNode objectNode=objectMapper.createObjectNode();
        objectNode.put("list", String.valueOf(integers));
        log.error(objectNode.toPrettyString());

    }

}
