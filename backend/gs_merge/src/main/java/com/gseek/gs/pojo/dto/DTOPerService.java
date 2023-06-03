package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.common.ParameterWrongException;

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
     * @throws ServerException 其他异常均视为服务器异常:
     *                         IllegalBlockSizeException | BadPaddingException | JsonProcessingException 来源于解密时.
     *                         Exception 为未知异常.
     * */
    default void perService()
            throws ParameterWrongException, ServerException{
        try {
            validateParameters();
            autoDecrypt();
        }catch (IllegalBlockSizeException | BadPaddingException | JsonProcessingException se){
            throw new ServerException("接收时出错", se);
        }catch (Exception e){
            throw new ServerException("服务器异常", e);
        }
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
    void validateParameters() throws ParameterWrongException, JsonProcessingException;

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
     * @throws IllegalBlockSizeException 解密时异常
     * @throws BadPaddingException 解密时异常
     * */
    default void autoDecrypt() throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {}
}
