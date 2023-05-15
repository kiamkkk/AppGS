package com.gseek.gs.pojo.dto;

import com.gseek.gs.exce.business.ParameterWrongException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * 包含dto传入service前要执行的操作.
 *
 * @author Phak
 * @since 2023/5/12-23:12
 */
public interface DTOPerService {


    /**
     * dto传入service前要执行的操作放在这里.
     * 现在包括：验参、解密.
     *
     * @throws ParameterWrongException 参数异常被封装在这里,具体格式查看ParameterWrongBean
     * //todo 这几个干什么用的？？？
     * @throws IllegalBlockSizeException 这干嘛的我不到啊
     * @throws BadPaddingException 这干嘛的我不到啊
     * */
    default void perService()
            throws IllegalBlockSizeException, BadPaddingException, ParameterWrongException {
        validateParameters();
        autoDecrypt();
    }

    /**
     * 执行这个方法进行验参.
     * 格式：
     * <code>
     *     ParameterWrongBean bean =new ParameterWrongBean();
     *
     *     if (.......){
     *         bean.addParameters("字段名", 字段值);
     *     }
     *     if (.......){
     *          bean.addParameters("字段名", 字段值);
     *     }
     *     if (.......){
     *          bean.addParameters("字段名", 字段值);
     *     }
     *     ......
     *
     *     if (! bean.getWrongParameters().isEmpty()){
     *             throw new ParameterWrongException(bean);
     *     }
     * </code>
     *
     * @throw ParameterWrongException 参数异常被封装在这里,具体格式查看ParameterWrongBean.
     * */
    void validateParameters() throws ParameterWrongException;

    /**
     * 验参结束后调用这个方法对有需要的字段进行解密.
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
    default void autoDecrypt() throws IllegalBlockSizeException, BadPaddingException{}
}
