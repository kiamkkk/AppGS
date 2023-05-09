package com.gseek.gs.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-12:11
 */
@SpringBootTest
public class DAOTest {

    @Autowired
    GoodCoverPicMapper goodCoverPicMapper;

    @Test
    public void test(){
        List<String> paths=new ArrayList<>();
        paths.add("/test/path1");
        paths.add("/test/path2");
        paths.add("/test/path3");
        goodCoverPicMapper.insertCoverPic(301, paths);
    }

}
