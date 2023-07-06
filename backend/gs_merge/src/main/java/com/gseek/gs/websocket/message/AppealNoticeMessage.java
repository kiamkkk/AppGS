package com.gseek.gs.websocket.message;

import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.service.inter.BuyerToSellerAppealService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Isabella
 * @create: 2023-05-24 23:21
 **/
public class AppealNoticeMessage extends AppealMessageBase{

    public AppealNoticeMessage(AppealMessageBean bo){
        super(SYSTEM_FROM_USER_ID,bo.getToUserId(),bo.getGoodName(),bo.getBillId(),MessageType.APPEAL.name(),"被申诉");
    }
    public AppealNoticeMessage(String message,AppealMessageBean bo){
        super(SYSTEM_FROM_USER_ID,bo.getToUserId(),"因撤销申诉",bo.getBillId(),  MessageType.APPEAL.name(), message);
    }
    @Override
    public void setMessage(String message) {
        super.setMessage(message);
    }

    @Override
    public void setTime(Long time) {
        super.setTime(time);
    }
}
