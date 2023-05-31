package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodAccountBO;
import com.gseek.gs.pojo.business.GoodBO;
import com.gseek.gs.pojo.business.GoodsWithoutAccountAndSoldBO;
import com.gseek.gs.pojo.data.GoodDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-12:38
 */
@Mapper
public interface GoodMapper {
    /**
     * 插入商品信息.
     * */
    int insertGood(@Param("goodDO") GoodDO goodDO);
    /**
     * 根据商品id,查出所有商品信息.
     * */
    GoodDO selectGoodByGoodIdFully(int goodId);
    /**
     * 根据商品id,获取除账号信息、账号密码信息、是否出售之外的商品信息.
     * @deprecated 使用selectGoodsByGoodIdsWithoutAccountAndSold,因为这个方法可以查询一个或多个数据.
     * */
    GoodsWithoutAccountAndSoldBO selectGoodByGoodIdWithoutAccount(@Param("goodId") int goodId);
    /**
     * 根据多个商品id,获取除账号信息、账号密码信息、是否出售之外的商品信息.
     * */
    List<GoodsWithoutAccountAndSoldBO> selectGoodsByGoodIdsWithoutAccountAndSold ( @Param("goodIds") List<Integer> goodIds );
    /**
     * 选择性更新商品信息.
     * 如果goodDO的某个字段为null,则不会更新对应的商品信息.
     * */
    int updateGoodSelect(@Param("good") GoodDO goodDO);

    /**
     * 根据商品id,删除商品.
     * 在service里确保商品正在出售时不执行这个操作.
     * 事务地删除good、good_tag、good_Fav、good_cover_pic、good_detail_pic、bill下的信息
     * 物理删除.
     * */
    @Transactional(rollbackFor = Exception.class)
    int deleteGood(@Param("goodId") int goodId);
    /**
     * 根据商品id,获取商品所有人id.
     * */
    Integer selectOwnUserIdByGoodId(@Param("goodId") int goodId);
    /**
     * 根据用户id,获取卖家所有的,除商品类型id之外的商品信息.
     * */
    List<GoodBO> selectGoodsByUserIdWithoutTypeTagId(@Param("userId") int userId);
    /**
     * 根据商品id,获取除商品类型id之外的商品信息.
     * */
    List<GoodBO> selectGoodsSoldByUserIdWithoutTypeTagId(@Param("userId") int userId);
    //todo 下面这两个方法应该挪到BillMapper里
    /**
     * 根据订单id,获取商品价格.
     * */
    BigDecimal selectPriceByBillId(@Param("billId") int billId);
    /**
     * 根据订单id,获取商品账号信息.
     * */
    GoodAccountBO selectGoodAccountByBillId(@Param("billId") int billId);

    List<GoodsWithoutAccountAndSoldBO> queryAllCheckedGood();
    List<GoodsWithoutAccountAndSoldBO> queryGoodByName(@Param("goodName") String goodName);

}
