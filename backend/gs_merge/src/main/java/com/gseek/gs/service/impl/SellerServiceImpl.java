package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.*;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.exce.business.seller.GoodSellingException;
import com.gseek.gs.pojo.bean.GoodPhotoFileBean;
import com.gseek.gs.pojo.bean.GoodPhotoPathBean;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.pojo.business.GoodBO;
import com.gseek.gs.pojo.business.OfferPriceBO;
import com.gseek.gs.pojo.data.GoodDO;
import com.gseek.gs.pojo.data.TagDO;
import com.gseek.gs.pojo.dto.PatchGoodsDTO;
import com.gseek.gs.pojo.dto.PostGoodsDTO;
import com.gseek.gs.service.inter.SellerService;
import com.gseek.gs.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-22:16
 */
@Slf4j
@Service("sellerServiceImpl")
public class SellerServiceImpl implements SellerService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MinioUtil minioUtil;
    @Autowired
    Result result;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    GoodCoverPicMapper goodCoverPicMapper;
    @Autowired
    GoodDetailPicMapper goodDetailPicMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    GoodTagMapper goodTagMapper;
    @Autowired
    OfferPriceMapper offerPriceMapper;
    @Autowired
    GoodCheckMapper goodCheckMapper;
    @Autowired
    BillMapper billMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String postGood(int userId, String userName, PostGoodsDTO dto) throws JsonProcessingException {
        TagDO type=tagMapper.selectTagByTagName(dto.getType());
        if (type==null){
            throw new ParameterWrongException(new ParameterWrongBean()
                    .addParameters("该商品类型不存在",dto.getType()));
        }

        GoodDO goodDO=new GoodDO(userId,userName,dto,type);
        goodMapper.insertGood(goodDO);
        int goodId=goodDO.getGoodId();

        if (goodId==0) {
            throw new ServerException("good主键不回显,发生时间{}",System.currentTimeMillis()+"");
        }
        // 更新type和tag情况
        updateTypeAndTags(dto,goodDO,goodId);
        // 把good加入待审核名单
        goodCheckMapper.insertNewGood(goodId);

        return result.gainPostSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String patchGood(int userId, String userName, PatchGoodsDTO dto)
            throws JsonProcessingException, ParameterWrongException {
        // 鉴权
        int ownUserId=goodMapper.selectOwnUserIdByGoodId(dto.getGoodId());
        if (ownUserId != userId){
            throw new ForbiddenException();
        }
        // 商品正在售出时禁止修改
        if (billMapper.selectBillByGoodId(dto.getGoodId()) != null ){
            throw new GoodSellingException();
        }

        GoodDO goodDO=new GoodDO(userName,dto);
        // 更新type和tag情况
        updateTypeAndTags(dto,goodDO);

        goodMapper.updateGoodSelect(goodDO);

        return result.gainPatchSuccess();
    }

    @Override
    public String deleteGood(int userId, int goodId) throws JsonProcessingException {

        // 鉴权
        Integer ownUserId=goodMapper.selectOwnUserIdByGoodId(goodId);
        if (userId != ownUserId){
            throw new ForbiddenException();
        }
        // 商品正在售出时禁止删除
        if (billMapper.selectBillByGoodId(goodId) != null ){
            throw new GoodSellingException();
        }
        goodMapper.deleteGood(goodId);

        return result.gainDeleteSuccess();
    }

    @Override
    public String getAllGoods(int userId) throws JsonProcessingException {
        List<GoodBO> bos=goodMapper.selectGoodsByUserIdWithoutTypeTagId(userId);
        return objectMapper.writeValueAsString(bos);
    }

    @Override
    public String getGoodsSold(int userId) throws JsonProcessingException {
        List<GoodBO> bos=goodMapper.selectGoodsSoldByUserIdWithoutTypeTagId(userId);
        return objectMapper.writeValueAsString(bos);
    }

    @Override
    public String getSingleGoodOfferPrice(int goodId) throws JsonProcessingException {
        List<OfferPriceBO> bos=offerPriceMapper.selectOfferPriceByGoodId(goodId);
        return objectMapper.writeValueAsString(bos);
    }

    //todo 补充注释
    // todo 下面这两个方法不要进行方法重载,因为这两个方法处理的业务不同
    /**
     * 修改商品时,处理商品类型、tag、图片储存路径.
     * 首先会清除商品对应的tag、图片路径信息.
     * 商品类型: 数据库中有这种类型,则记录入good表中; 没有则抛出异常.
     * tag: 数据库中没有这种tag会在tag表中新建,然后获取所有tag的tagId.最后一并记录入good_tag表中.
     * 图片: 在minio中插入后,记录图片路径.
     * */
    private void updateTypeAndTags(PatchGoodsDTO dto,GoodDO goodDO) throws JsonProcessingException {
        int goodId=goodDO.getGoodId();
        String patchType=dto.getType();
        //todo 为什么我要在这再验一遍参数???
        if (patchType != null && !patchType.isBlank()){
            TagDO localType=tagMapper.selectTagByTagName(patchType);
            if (localType == null ){
                throw new ParameterWrongException(new ParameterWrongBean()
                        .addParameters("该商品类型不存在",patchType));
            }
            goodDO.setTypeTagId(localType.getTagId());
            goodDO.setTypeTagName(localType.getTagText());
        }

        //修改tag
        List<TagDO> tags=new ArrayList<>();
        if (dto.getTag() !=null && !dto.getTag().isEmpty()){
            for (String tagName:dto.getTag()){
                tags.add(new TagDO(tagName));
            }
            //数据库中没有的tag新建
            if (tags.size() !=0){
                tagMapper.insertTags(tags);
            }
            //数据库中已有的tag查询
            for (TagDO tagDO:tags){
                if (tagDO.getTagId()==null){
                    //todo 这太逆天了，就不能放在一次查吗
                    tagDO.setTagId(tagMapper.selectTagByTagName(tagDO.getTagText()).getTagId());
                }
            }

            goodTagMapper.updateGoodTag(goodId,tags);
        }

        //储存图片
        GoodPhotoPathBean coverAndDetailPath=minioUtil.saveGoodsPhoto(new GoodPhotoFileBean(
                goodId,dto.getCoverPicture(),dto.getDetailPictures())
        );

        goodCoverPicMapper.insertCoverPic(goodId,coverAndDetailPath.getCoverPaths());
        goodDetailPicMapper.insertDetailPic(goodId,coverAndDetailPath.getDetailPaths());
        // 更新商品信息
        goodMapper.updateGoodSelect(goodDO);
    }
    /**
     * 新建商品时,处理商品类型、tag、图片储存路径.
     * 商品类型: 数据库中有这种类型,则记录入good表中; 没有则抛出异常.
     * tag: 数据库中没有这种tag会在tag表中新建,然后获取所有tag的tagId.最后一并记录入good_tag表中.
     * 图片: 在minio中插入后,记录图片路径.
     *
     * @param dto
     * @param goodDO
     * @param goodId 商品id
     * */
    private void updateTypeAndTags(PostGoodsDTO dto, GoodDO goodDO, int goodId) throws JsonProcessingException {
        String patchType=dto.getType();

        // 处理商品类型
        TagDO localType=tagMapper.selectTagByTagName(patchType);
        if (localType == null ){
            throw new ParameterWrongException(new ParameterWrongBean()
                    .addParameters("该商品类型不存在",patchType));
        }
        goodDO.setTypeTagId(localType.getTagId());
        goodDO.setTypeTagName(localType.getTagText());


        //修改tag
        List<TagDO> tags=new ArrayList<>();
        if (dto.getTag() !=null && !dto.getTag().isEmpty()){
            for (String tagName:dto.getTag()){
                tags.add(new TagDO(tagName));
            }
            // 如果数据库中没有该tag,会新建;已有则忽略插入操作
            if (tags.size() !=0 ){
                tagMapper.insertTags(tags);
            }
            //数据库中已有的tag查询
            for (TagDO tagDO:tags){
                if (tagDO.getTagId() == null){
                    //todo 这太逆天了，就不能放在一次查吗
                    tagDO.setTagId(tagMapper.selectTagByTagName(tagDO.getTagText()).getTagId());
                }
            }

            goodTagMapper.updateGoodTag(goodId,tags);
        }

        //储存图片
        GoodPhotoPathBean coverAndDetailPath=minioUtil.saveGoodsPhoto(new GoodPhotoFileBean(
                goodId,dto.getCoverPicture(),dto.getDetailPictures())
        );

        goodCoverPicMapper.insertCoverPic(goodId,coverAndDetailPath.getCoverPaths());
        goodDetailPicMapper.insertDetailPic(goodId,coverAndDetailPath.getDetailPaths());
        // 更新商品信息
        //todo 应该在插入新商品信息(71行)前向goodDO中加入type信息,而不是在这里.
        goodMapper.updateGoodSelect(goodDO);
    }


}
