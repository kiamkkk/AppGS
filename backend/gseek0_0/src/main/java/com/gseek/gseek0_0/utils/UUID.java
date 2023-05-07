package com.gseek.gseek0_0.utils;

public class UUID {
    public static String getUUID(){
        return java.util.UUID.randomUUID().toString().replaceAll("-","");
    }
}
