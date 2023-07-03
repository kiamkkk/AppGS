package com.gseek.gs.exce.appeal;

import com.gseek.gs.exce.business.BusinessException;

/**
 * @author: Isabella
 * @create: 2023-06-20 00:49
 **/
public class AlreadyAuditedException extends BusinessException {
    public AlreadyAuditedException(){
        super("已经被审核无法修改");
        code=403;
        msg="already been audited";
    }
}
