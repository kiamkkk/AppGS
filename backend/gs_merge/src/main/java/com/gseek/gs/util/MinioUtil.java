package com.gseek.gs.util;

import com.gseek.gs.config.MinioConfig;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.pojo.bean.CoverOrDetailPhotoFileBean;
import com.gseek.gs.pojo.bean.GoodPhotoFileBean;
import com.gseek.gs.pojo.bean.GoodPhotoPathBean;
import com.gseek.gs.websocket.service.AnnounceService;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author Phak
 * @since 2023/5/2-21:09
 */
@Component
@Slf4j
public class MinioUtil {
    // todo 对文件进行操作时检查文件是否存在
    //todo RuntimeException替换为ServletException
    //todo 增、删、改、根据路径获取文件流。要有批量操作。
    /**
     * jpg文件后缀
     * */
    public static final String SUFFIX_JPG=".jpg";
    /**
     * 头像储存路径
     * */
    public static final String PATH_HEAD_SCULPTURES="imgs/profile_photos/";
    /**
     * 默认头像
     * */
    public static final String DEFAULT_PROFILE_PHOTO="imgs/profile_photos/default_profile_photo.jpg";
    /**
     * 黑名单头像
     * */
    public static final String BLACKLIST_PROFILE_PHOTO="imgs/profile_photos/blacklist_profile_photo.jpg";
    /**
     * 商品图片总储存路径
     * */
    public static final String PATH_GOOD_PICTURE="imgs/goods/";
    /**
     * 聊天记录图片储存路径
     * */
    public static final String PATH_CHATS="imgs/chats/";

    /**
     * 储存商品封面时,路径的一部分
     * 使用例: /imgs/goods/{商品id}/cover/{图片名}.jpg
     * */
    public static final String PATH_GOOD_COVER="/covers/";
    /**
     * 储存商品详情图片时,路径的一部分
     * 使用例: /imgs/goods/{商品id}/detail/{图片名}.jpg
     * */
    public static final String PATH_GOOD_DETAIL="/details/";
    @Autowired
    private MinioConfig prop;
    @Autowired
    private MinioClient minioClient;

    /**
     * 修改头像.
     * 先删除原有头像，后存入新头像.
     *
     * @param userId 用户id
     * @param file 新头像
     * @return 图片路径
     */
    public String changeProfilePhoto(int userId, MultipartFile file)
            throws  com.gseek.gs.exce.ServerException {
        if (file==null) {
            return DEFAULT_PROFILE_PHOTO;
        }
        removeFile(userId+SUFFIX_JPG, PATH_HEAD_SCULPTURES);
        return saveProfilePhoto(userId, file);
    }
    /**
     * 储存头像.
     *
     * @param userId 用户id
     * @param profilePhoto 头像
     * */
    public String saveProfilePhoto(int userId, MultipartFile profilePhoto)
            throws com.gseek.gs.exce.ServerException {

        if (profilePhoto==null) {
            return DEFAULT_PROFILE_PHOTO;
        }else {
            putFile(profilePhoto,PATH_HEAD_SCULPTURES,userId+SUFFIX_JPG);
            return PATH_HEAD_SCULPTURES+userId+SUFFIX_JPG;
        }

    }


    /**
     * 储存商品封面与详情图片.
     * */
    public GoodPhotoPathBean saveGoodPhotos(GoodPhotoFileBean bean){
        Integer goodId=bean.getGoodId();

        List<String> coverPaths = saveGoodCoverOrDetail(
                new CoverOrDetailPhotoFileBean(goodId, bean.getCovers()) , PATH_GOOD_COVER
        );
        List<String> detailPaths = saveGoodCoverOrDetail(
                new CoverOrDetailPhotoFileBean(goodId, bean.getDetails()) , PATH_GOOD_DETAIL
        );

        return new GoodPhotoPathBean(goodId,coverPaths,detailPaths);
    }

    /**
     * 删除商品旧有的全部图片.
     * */
    public void removeGoodPhotos(GoodPhotoFileBean bean){
        // 查询商品下所有图片路径
        List<String> names = listFiles(PATH_GOOD_PICTURE+bean.getGoodId()+PATH_GOOD_COVER);
        names.addAll(
                listFiles(PATH_GOOD_PICTURE+bean.getGoodId()+PATH_GOOD_DETAIL)
        );
        // 删除
        removeFile(names);
    }

    /**
     * 修改商品封面与详情图片.
     * 先全部删除旧有的图片,再储存传入的图片.
     * */
    public GoodPhotoPathBean updateGoodPhotos(GoodPhotoFileBean bean){
        // 查询商品下所有图片路径
        List<String> names = listFiles(PATH_GOOD_PICTURE+bean.getGoodId()+PATH_GOOD_COVER);
        names.addAll(
                listFiles(PATH_GOOD_PICTURE+bean.getGoodId()+PATH_GOOD_DETAIL)
        );
        // 删除
        removeFile(names);
        // 保存新图片
        return saveGoodPhotos(bean);
    }
    /**
     * 储存聊天时图片.
     * */
    public String saveChatPicture(MultipartFile pic,long time,int goodId,int fromUserId)
            throws com.gseek.gs.exce.ServerException {
        String[] pathAndFileName=createChatImgPathAndFileName(goodId,fromUserId,time);
        putFile(pic, pathAndFileName[0],pathAndFileName[1]);
        return pathAndFileName[0]+pathAndFileName[1];
    }
    /**
     * 下载文件
     * */
    public void downloadImg(HttpServletResponse response,String path,String filename)
            throws com.gseek.gs.exce.business.imgs.FileNotFoundException {
        InputStream file = getFile(path,filename);
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");

        try(OutputStream outputStream = response.getOutputStream()) {
            int len ;
            byte[] buffer = new byte[1024];
            while((len=file.read(buffer)) > 0){
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    /**
     * 读取所有公告的文件名.
     * */
    public List<String> listAnnounce(){
        return listFiles(AnnounceService.ANNOUNCE_PATH);
    }

    /**
     * 储存封面图片或详情图片.
     *
     * @param bean 图片文件、商品id
     * @param coverOrDetail 表示要存的是封面还是详情图片,必须使用常量PATH_GOOD_COVER或PATH_GOOD_DETAIL
     * @return photoPaths 每张图片的储存路径
     * */
    private List<String> saveGoodCoverOrDetail(CoverOrDetailPhotoFileBean bean, String coverOrDetail){
        assert (
                PATH_GOOD_COVER.equals(coverOrDetail) ||
                        PATH_GOOD_DETAIL.equals(coverOrDetail)
        ) : "coverOrDetail必须使用常量PATH_GOOD_COVER或PATH_GOOD_DETAIL";

        // todo 用bean传参!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Map<Map<String,String>,MultipartFile> photoBean=new HashMap<>(16);
        Map<String,String> photoPathAndName=new HashMap<>(16);
        int goodId=bean.getGoodId();
        List<MultipartFile> pic=bean.getPhotos();
        List<String> photoPaths=new ArrayList<>();
        for (int i=0; i < pic.size(); i++){
            photoPathAndName.put(PATH_GOOD_PICTURE+goodId+coverOrDetail,i+SUFFIX_JPG);
            photoPaths.add(PATH_GOOD_PICTURE+goodId+coverOrDetail+i+SUFFIX_JPG);
            photoBean.put(photoPathAndName, pic.get(i));
        }
        putFile(photoBean);

        return photoPaths;
    }
    /**
     * 构造聊天图片记录路径和文件名
     * */
    private String[] createChatImgPathAndFileName(int goodId,int fromUserId,long time){
        String[] pathAndFileName=new String[2];
        pathAndFileName[0]=PATH_CHATS + goodId + "/" + fromUserId + "/";
        pathAndFileName[1]=fromUserId+"_"+time+SUFFIX_JPG;
        return pathAndFileName;
    }
    /**
     * 储存单个文件
     *
     * @param file 文件
     * @param path 储存路径
     * @param fileName 储存为文件名
     * */
    private void putFile(MultipartFile file,String path,String fileName) throws com.gseek.gs.exce.ServerException{
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(prop.getBucketName())
                            .object(path+fileName)
                            .stream(file.getInputStream(), file.getSize(),-1)
                            .contentType(file.getContentType())
                            .build()
            );
        }catch (Exception e){
            throw new com.gseek.gs.exce.ServerException("服务器异常", e);
        }
    }
    /**
     * 储存多个文件.
     *
     * @param files 文件名,储存路径与文件:
     *              <code>Map(Map(储存路径,文件名),文件)</code>
     * */
    private void putFile(Map<Map<String,String>,MultipartFile> files) {
        //todo 用bean传参！！！！！！！！！！！！！！！！！！！
        List<SnowballObject> objects = new ArrayList<>();
        for (Map.Entry<Map<String,String>,MultipartFile> outEntry : files.entrySet()){
            for (Map.Entry<String,String> innerEntry : outEntry.getKey().entrySet()){
                try {
                    objects.add(
                            new SnowballObject(
                                    innerEntry.getKey()+innerEntry.getValue(),
                                    new ByteArrayInputStream(outEntry.getValue().getBytes()),
                                    outEntry.getValue().getSize(),
                                    null));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            minioClient.uploadSnowballObjects(
                    UploadSnowballObjectsArgs.builder()
                            .bucket(prop.getBucketName())
                            .objects(objects)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取单个文件
     *
     * @param path 储存路径
     * @param fileName 文件名
     * */
    public InputStream getFile(String path, String fileName)
            throws com.gseek.gs.exce.business.imgs.FileNotFoundException {
        if (! isFileExist(path, fileName)){
            throw new com.gseek.gs.exce.business.imgs.FileNotFoundException();
        }
        try {
           return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(prop.getBucketName())
                        .object(path+fileName)
                        .build()
            );
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取某个文件夹下所有文件文件名.
     *
     * @param path 文件夹名
     * */
    private List<String> listFiles(String path){
        List<String> objectNames=new ArrayList<>();

        Iterable<Result<Item>> iterable = minioClient
                .listObjects(
                    ListObjectsArgs.builder()
                            .bucket(prop.getBucketName())
                            .prefix(path)
                            .build()
                );

        try {
            for (Result<Item> result : iterable ) {
                objectNames.add(result.get().objectName());
            }
        }catch (Exception e){
            throw new ServerException("服务器异常", e);
        }

        return objectNames;
    }
    /**
     * 删除单个文件
     * 我不希望这个方法对外暴露,但是需要对这个方法进行单元测试.
     *
     * @param filename 文件名
     * @param path 储存路径
     * */
    protected void removeFile(String filename,String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(prop.getBucketName())
                            .object(path+filename)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 删除多个文件
     * 我不希望这个方法对外暴露,但是需要对这个方法进行单元测试.
     *
     * @param objectNames 路径名+文件名
     * */
    protected void removeFile(List<String> objectNames) {
        //todo 用bean传参！！！！！！！！！！！！！！！！！！！
        List<DeleteObject> objects = new LinkedList<>();
        for (String name : objectNames){
            objects.add(new DeleteObject(name));
        }

        Iterable<Result<DeleteError>> results =
                minioClient.removeObjects(
                        RemoveObjectsArgs.builder().bucket(prop.getBucketName()).objects(objects).build());

        //我真搞不懂为什么还要遍历一遍它才肯删...
        for (Result<DeleteError> result : results) {
            DeleteError error = null;
            try {
                error = result.get();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * 检查文件是否存在
     * 我不希望这个方法对外暴露,但是MinioUtilTest中需要这个方法来检查其他方法是否成功执行.
     * */
    protected boolean isFileExist(String path, String fileName){
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(prop.getBucketName())
                            .object(path+fileName)
                            .build()
            );
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
