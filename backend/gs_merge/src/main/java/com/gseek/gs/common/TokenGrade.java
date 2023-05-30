package com.gseek.gs.common;

import java.util.HashMap;
import java.util.Map;

/**
 * token权限级别，是用户或管理员
 *
 * @author Phak
 * @since 2023/5/22-22:17
 */
public enum TokenGrade {


    /**
     * token权限级别为用户
     * */
    USER,

    /**
     * token权限级别为管理员
     * */
    ADMIN;

    private static Map<String,TokenGrade> map= new HashMap(2);

    static {
        for (TokenGrade grade:TokenGrade.values()){
            map.put(grade.name(), grade);
        }
    }

    /**
     * 根据名字获取枚举.
     *
     * @param name 枚举名
     * */
    public static TokenGrade gainTokenGradeByName(String name){
        return map.get(name);
    }
}
