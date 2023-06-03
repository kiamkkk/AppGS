package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.MoneyBO;
import com.gseek.gs.pojo.data.MoneyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 操作money表.
 *
 * @author Phak
 * @since 2023/5/2-20:25
 */
@Mapper
public interface MoneyMapper {

    /**
     * 插入新一般用户的余额信息.
     * 注册用户的第四个步骤.
     * */
    int insertMoney(@Param("moneyDO") MoneyDO moneyDO);

    /**
     * 将用户的余额减去 price.
     * 使用这个方法之前应该检查用户余额是否足够.
     * */
    int minusRemainByUserId(@Param("userId") int userId,@Param("price") BigDecimal price);

    /**
     * 将用户的余额加上 price.
     * */
    int addRemainByUserId(@Param("userId") int userId,@Param("price") BigDecimal price);
    /**
     * 根据订单进行退款.
     * 先将买家余额增加商品价格的某个倍数,再将卖家余额减去商品价格.
     * 使用这个方法之前检查卖家余额是否足够.
     * 具体乘以什么倍数得看mapper,我也不知道为什么是这个数.
     * */
    int returnMoney(@Param("billId") int billId);

    /**
     * 根据订单进行付款.
     * 先将卖家余额增加商品价格,再将买家余额减去商品价格的某个倍数.
     * 使用这个方法之前检查买家余额是否足够.
     * 具体乘以什么倍数得看mapper,我也不知道为什么是这个数.
     * */
    int payToSeller(@Param("billId") int billId);

    /**
     * 将卖家的所有余额转到买家名下.
     * 先将买家余额加上卖家现有余额,再将卖家余额设为0.
     * */
    int updateAllRemainToBuyer(@Param("billId") int billId);

    /**
     * 根据用户id,获取余额
     * */
    BigDecimal selectRemainByUserId(@Param("userId") int userId);

    /**
     * 根据用户id,获取用户余额.息
     * */
    MoneyBO selectMoneyBOByUserId(@Param("userId") int userId);
    int frozenUser(int userId);
    int unfrozenUser(int userId);
    int returnMoneyByDegree(@Param("billId") int billId,@Param("rate") BigDecimal rate);
    int returnBuyerAppealMoney(int billId);
    int returnSellerAppealMoney(int billId);

}
