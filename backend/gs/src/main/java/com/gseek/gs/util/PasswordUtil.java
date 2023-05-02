package com.gseek.gs.util;

import org.springframework.stereotype.Component;

/**
 * @author Phak
 * @since 2023/5/2-21:47
 */
@Component
public class PasswordUtil {


    /**
     *
     * */
    public String gainSalt(){
       return "397924423F4528482B4D6251655468576D5A7134743777217A25432A46294A40";
    }

    /**
     *
     * */
    public String decode(String toDecode){
       return  toDecode;
    }

}
