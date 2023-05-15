package com.gseek.gs.pojo.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * 包含BO传出service前要执行的操作.
 * //todo 这是不是每个BO都必须实现的？？
 *
 * @author Phak
 * @since 2023/5/13-18:41
 */
public interface BOPostService {


    /**
     * BO传出service前必须要执行的操作放在这里.
     * 现在包括：加密.
     *
     * //todo 这几个干什么用的？？？
     * @throws IllegalBlockSizeException 这干嘛的我不到啊
     * @throws BadPaddingException 这干嘛的我不到啊
     * */
    default void postService() throws IllegalBlockSizeException, BadPaddingException{
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
     * //todo 这几个干什么用的？？？
     * @throws IllegalBlockSizeException 这干嘛的我不到啊
     * @throws BadPaddingException 这干嘛的我不到啊
     * */
    default void autoEncrypt() throws IllegalBlockSizeException, BadPaddingException{}

    /**
     * 将对象本身包装成json返回.
     *
     * @param objectMapper 在service里自动注入一个objectMapper,然后传入这里
     * @return json
     */
    default String autoJson(ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
