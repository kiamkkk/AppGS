package com.gseek.gs.pojo.business;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * 包含BO传出service前要执行的操作.
 *
 * @author Phak
 * @since 2023/5/13-18:41
 */
public interface BOPostService {


    /**
     * BO传出service前必须要执行的操作放在这里.
     * 现在包括：加密.
     *
     * @throws IllegalBlockSizeException 解密时异常
     * @throws BadPaddingException 解密时异常
     * */
    default void postService() throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {
        autoEncrypt();
    }

    /**
     * 调用这个方法对有需要的字段进行加密.
     * 格式：
     * <code>
     *     aaa=PasswordUtil.(aaa);
     *     bbb=PasswordUtil.(bbb);
     *     ccc=PasswordUtil.(ccc);
     *     ......
     * </code>
     *
     * @throws IllegalBlockSizeException 解密时异常
     * @throws BadPaddingException 解密时异常
     * */
    default void autoEncrypt() throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {}

}
