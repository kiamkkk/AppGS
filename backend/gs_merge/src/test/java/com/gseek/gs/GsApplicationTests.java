package com.gseek.gs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GsApplicationTests {

    @Test
    void contextLoads() {
        List<String> oldStr=new ArrayList<>();
        oldStr.add("qwe");
        oldStr.add("das");
        oldStr.add("ryt");
        oldStr.add("woij");
        oldStr.add("wdsadsaj");
        int sice=oldStr.size();

        List<String> newStr=new ArrayList<>();
        for (int i=0;i<sice;i++){
            if (i==1){
                newStr.add(oldStr.get(i));
                oldStr.remove(i);
                System.err.println(sice);
            }
        }
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(oldStr);
        System.out.println(newStr);
        System.out.println("--------------------------------------------------------------------------------");
    }

}
