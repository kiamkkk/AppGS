package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.*;
import com.gseek.gs.exce.business.common.ForbiddenException;
import com.gseek.gs.exce.business.common.ParameterWrongException;
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
import org.springframework.web.multipart.MultipartFile;

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
        // 只允许插入已有type
        TagDO type=tagMapper.selectTagByTagName(dto.getType());
        if (type==null){
            throw new ParameterWrongException(new ParameterWrongBean()
                    .addParameters("该商品类型不存在",dto.getType()));
        }

        // 记录商品信息
        GoodDO goodDO=new GoodDO(userId,userName,dto,type);
        goodMapper.insertGood(goodDO);

        Integer goodId=goodDO.getGoodId();
        // 记录商品与tag关系
        recordGoodToTags(dto.getTag(), goodId);

        // 新增图片
        saveGoodPhotos(goodId, dto.getCoverPicture(), dto.getDetailPictures());

        // 把good加入待审核名单
        goodCheckMapper.insertNewGood(goodId);


        return result.gainPostSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String patchGood(int userId, String userName, PatchGoodsDTO dto)
            throws JsonProcessingException, ParameterWrongException {
        int goodId=dto.getGoodId();
        GoodDO goodDO=new GoodDO(userName,dto);
        // 非商品所有人不能操作
        if (goodMapper.selectOwnUserIdByGoodId(goodId) != userId){
            throw new ForbiddenException();
        }
        // 商品正在交易时禁止修改
        if (isGoodSelling(goodId)){
            throw new GoodSellingException();
        }

        // 确定type信息要不要修改
        String oldType = dto.getType();
        if ( oldType ==null || oldType.isBlank() ){
            // 只允许插入已有type
            TagDO type=tagMapper.selectTagByTagName(oldType);
            if (type==null){
                throw new ParameterWrongException(new ParameterWrongBean()
                        .addParameters("该商品类型不存在",dto.getType()));
            }
            goodDO.setTypeTagId(type.getTagId());
            goodDO.setTypeTagName(type.getTagText());
        }

        // 更新商品信息
        goodMapper.updateGoodSelect(goodDO);

        // 更新商品与tag关系: 先删除全部tag信息,后存入新信息
        goodTagMapper.deleteAllGoodTagsByGoodId(goodId);
        recordGoodToTags(dto.getTag(), goodId);

        // 更新图片情况
        // 先删除
        minioUtil.removeGoodPhotos( new GoodPhotoFileBean(
                goodId, dto.getCoverPicture(), dto.getDetailPictures()
            )
        );
        // 后新增
        saveGoodPhotos(goodId, dto.getCoverPicture(), dto.getDetailPictures());

        return result.gainPatchSuccess();
    }

    @Override
    public String deleteGood(int userId, int goodId) throws JsonProcessingException {
        // 非商品所有人不能操作
        if (goodMapper.selectOwnUserIdByGoodId(goodId) != userId){
            throw new ForbiddenException();
        }
        // 商品正在交易时禁止修改
        if (isGoodSelling(goodId)){
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

    /**
     * 记录商品与tag的关系.
     * 先确定数据库中是否已有tag,没有则在tag表里新增; 再记录tag与商品的关系.
     *
     * @param goodId 商品id
     * @param tagNames tag内容
     * */
    private void recordGoodToTags(List<String> tagNames, int goodId){

        if (tagNames !=null && ! tagNames.isEmpty()){
            List<TagDO> tags=new ArrayList<>();
            for (String tagName: tagNames){
                tags.add(new TagDO(tagName));
            }
            // 如果数据库中没有该tag,会新建;已有则忽略插入操作
            tagMapper.insertTags(tags);
            // 确定有无新增tag
            List<String> newTags=new ArrayList<>();
            TagDO tagDO;
            // 使用固定值tagsSize作为边界条件,使得移除tags的元素后循环正常进行
            int tagsSize=tags.size();
            for (int i=0 ; i < tagsSize;i++){
                tagDO=tags.get(i);
                if ( tagDO.getTagId() == null ){
                    newTags.add(tagDO.getTagText());
                    tags.remove(i);
                }
            }

            // 如果有新tag, 把新tag加入tags中
            if (newTags.size() > 0){
                List<TagDO> oldTags = tagMapper.selectTagsByTagNames(newTags);
                tags.addAll(oldTags);
            }

            goodTagMapper.updateGoodTag(goodId,tags);
        }
    }

    /**
     * 向minio中插入新图片,记录图片路径
     * */
    private void saveGoodPhotos(int goodId, List<MultipartFile> coverPics, List<MultipartFile> detailPics){
        // 向minio插入图片
        GoodPhotoPathBean bean = minioUtil.saveGoodPhotos(
                new GoodPhotoFileBean(
                        goodId, coverPics, detailPics
                )
        );

        // 记录路径
        List<String> coverPaths=bean.getCoverPaths();
        List<String> detailPaths=bean.getCoverPaths();

        if (coverPaths.size() > 0){
            goodCoverPicMapper.insertCoverPic(goodId, coverPaths);
        }
        if (detailPaths.size() > 0){
            goodDetailPicMapper.insertDetailPic(goodId, detailPaths);
        }
    }

    /**
     * 确定商品是否正在交易
     * */
    private boolean isGoodSelling(int goodId){
        return billMapper.selectBillByGoodId(goodId) != null;
    }

}
