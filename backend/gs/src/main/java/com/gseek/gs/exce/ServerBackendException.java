package com.gseek.gs.exce;

import java.util.Arrays;

/**
 * 后端错误.
 *
 * @author Phak
 * @since 2023/5/3-14:18
 */
public class ServerBackendException extends BaseException{

    public ServerBackendException(String... info) {
        super(info);
        code=50001;
        msg= Arrays.toString(info);
    }

}
