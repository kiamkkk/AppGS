package com.gseek.gs.exce;

import java.util.Arrays;

/**
 * 服务器错误.
 *
 * @author Phak
 * @since 2023/5/3-14:18
 */
public class ServerException extends BaseException{

    public ServerException(String... info) {
        super(info);
        code=50001;
        msg= Arrays.toString(info);
    }

    public ServerException(Throwable cause) {
        super(cause);
        code=50001;
        msg=cause.getMessage();
    }
}
