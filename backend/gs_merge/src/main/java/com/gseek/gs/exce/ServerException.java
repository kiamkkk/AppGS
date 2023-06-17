package com.gseek.gs.exce;

/**
 * 服务器错误.
 * 表示不由用户给出数据引起的、服务器自身的问题.
 * - 不应该在字段<code>msg</code>中给出详细信息,因为这类问题不是用户能够处理的.
 * - 应该在字段<code>msg</code>中给出异常出现的大致环节,方便后端定位;
 *   如是服务器自身问题,就直接写服务器异常.
 *
 * @author Phak
 * @since 2023/5/3-14:18
 */
public class ServerException extends BaseException{

    public ServerException(String message) {
        super(message);
        code=50001;
    }

    /**
     * 推荐使用这个构造方法.
     *
     * @param cause 系统抛出的异常.
     * @param msg 给用户看的错误信息.
     *            - 应该为异常出现的大致环节,如登录时异常、支付时异常、聊天时异常.
     *            - 如是服务器自身问题,就直接写服务器异常.
     * */
    public ServerException(String msg, Throwable cause) {
        super(msg, cause);
        code=500;
        this.msg=msg;
    }
}
