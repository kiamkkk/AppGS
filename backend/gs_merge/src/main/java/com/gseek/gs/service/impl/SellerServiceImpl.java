package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.GoodCoverPicMapper;
import com.gseek.gs.dao.GoodDetailPicMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.pojo.bean.GoodPhotoFileBean;
import com.gseek.gs.pojo.bean.GoodPhotoPathBean;
import com.gseek.gs.pojo.data.GoodDO;
import com.gseek.gs.pojo.dto.PatchGoodsDTO;
import com.gseek.gs.pojo.dto.PostGoodsDTO;
import com.gseek.gs.service.inter.SellerService;
import com.gseek.gs.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Phak
 * @since 2023/5/9-22:16
 */
@Slf4j
@Service("sellerServiceImpl")
public class SellerServiceImpl implements SellerService {
    //todo 只能持有人访问这些接口

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String postGood(int userId, String userName, PostGoodsDTO dto) throws JsonProcessingException {

        GoodDO goodDO=new GoodDO(userId,userName,dto);
        goodMapper.insertGood(goodDO);
        int goodId=goodDO.getGoodId();

        if (goodId==0) {
            throw new RuntimeException("good主键不回显|发生时间"+System.currentTimeMillis());
        }
        //todo 实现这两个
        //修改类型


        //修改tag

        //储存图片
        GoodPhotoPathBean coverAnddetailPath=minioUtil.saveGoodsPhoto(new GoodPhotoFileBean(
                goodId,dto.getCoverPicture(),dto.getDetailPictures())
        );

        goodCoverPicMapper.insertCoverPic(goodId,coverAnddetailPath.getCoverPaths());
        goodDetailPicMapper.insertDetailPic(goodId,coverAnddetailPath.getDetailPaths());

        return result.gainPostSuccess();
    }

    @Override
    public String patchGood(int userId, String userName, PatchGoodsDTO dto) throws JsonProcessingException {
        GoodDO goodDO=new GoodDO(userName,dto);
        int goodId=dto.getGoodId();
        //todo 实现这两个
        //修改类型

        //修改tag

        GoodPhotoPathBean coverAnddetailPath=minioUtil.saveGoodsPhoto(new GoodPhotoFileBean(
                goodId,dto.getCoverPicture(),dto.getDetailPictures())
        );

        goodCoverPicMapper.insertCoverPic(goodId,coverAnddetailPath.getCoverPaths());
        goodDetailPicMapper.insertDetailPic(goodId,coverAnddetailPath.getDetailPaths());

        return result.gainPatchSuccess();
    }
}
