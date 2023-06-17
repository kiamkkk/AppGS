package com.gseek.gs.exce.business.imgs;

import com.gseek.gs.exce.business.BusinessException;

/**
 * @author Phak
 * @since 2023/6/1-16:41
 */
public class FileNotFoundException extends BusinessException {

    public FileNotFoundException() {
        super("文件不存在");
        code=404;
        msg="FileNotFound";
    }
}