package com.gseek.gs.util;

import com.gseek.gs.config.MinioConfig;
import com.gseek.gs.pojo.bean.GoodPhotoFileBean;
import com.gseek.gs.pojo.bean.GoodPhotoPathBean;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Phak
 * @since 2023/5/2-21:09
 */
@Component
public class MinioUtil {
    //todo 增、删、改、根据路径获取文件流。要有批量操作。

    /**
     * jpg文件后缀
     * */
    public static final String SUFFIX_JPG=".jpg";
    /**
     * 头像储存路径
     * */
    public static final String PATH_HEAD_SCULPTURES="/imgs/profile_photos/";
    /**
     * 默认头像
     * */
    public static final String DEFAULT_PROFILE_PHOTO="/imgs/profile_photos/default_profile_photo.jpg";
    /**
     * 黑名单头像
     * */
    public static final String BLACKLIST_PROFILE_PHOTO="/imgs/profile_photos/blacklist_profile_photo.jpg";
    /**
     * 商品图片总储存路径
     * */
    public static final String PATH_GOOD_PICTURE="/imgs/goods/";
    /**
     * 聊天记录图片储存路径
     * */
    public static final String PATH_CHATS="/imgs/chats/";

    @Autowired
    private MinioConfig prop;

    @Autowired
    private MinioClient minioClient;

    /**
     * 储存头像.
     * */
    public String saveProfilePhoto(int userId, MultipartFile profilePhoto){

        if (profilePhoto==null) {
            return DEFAULT_PROFILE_PHOTO;
        }else {
            putFile(profilePhoto,PATH_HEAD_SCULPTURES,userId+SUFFIX_JPG);
            return PATH_HEAD_SCULPTURES+userId+SUFFIX_JPG;
        }

    }
    /**
     * 修改头像.
     * 先删除原有头像，后存入新头像.
     *
     * @param userId 用户id
     * @param file 新头像
     * @return 图片路径
     */
    public String changeProfilePhoto(int userId, MultipartFile file){
        removeFile(userId+SUFFIX_JPG, PATH_HEAD_SCULPTURES);
        return saveProfilePhoto(userId, file);
    }
    /**
     * 储存商品封面详情图片.
     * */
    public GoodPhotoPathBean saveGoodsPhoto(GoodPhotoFileBean bean){
        Map<Map<String,String>,MultipartFile> covers=new HashMap<>(16);
        Map<Map<String,String>,MultipartFile> details=new HashMap<>(16);
        Map<String,String> coverPathAndName=new HashMap<>(16);
        Map<String,String> detailPathAndName=new HashMap<>(16);

        int goodId=bean.getGoodId();
        List<MultipartFile> coverPic=bean.getCovers();
        List<MultipartFile> detailPic=bean.getDetails();

        List<String> coverPaths=new ArrayList<>();
        List<String> detailPaths=new ArrayList<>();

        for (int i=0; i <= coverPic.size(); i++){
            coverPathAndName.put(PATH_GOOD_PICTURE+goodId+"/covers/",i+SUFFIX_JPG);
            coverPaths.add(PATH_GOOD_PICTURE+goodId+"/covers/"+i+SUFFIX_JPG);
            covers.put(coverPathAndName, coverPic.get(i));
        }

        for (int j=0; j <= detailPic.size(); j++){
            detailPathAndName.put(PATH_GOOD_PICTURE+goodId+"/details/",j+SUFFIX_JPG);
            detailPaths.add(PATH_GOOD_PICTURE+goodId+"/details/"+j+SUFFIX_JPG);
            details.put(detailPathAndName, detailPic.get(j));
        }

        putFile(covers);
        putFile(details);

        return new GoodPhotoPathBean(goodId,coverPaths,detailPaths);
    }
    /**
     * 储存聊天时图片.
     * */
    public String saveChatPicture(MultipartFile pic,long time,int goodId,int fromUserId){
        String[] pathAndFileName=createChatImgPathAndFileName(goodId,fromUserId,time);
        putFile(pic, pathAndFileName[0],pathAndFileName[1]);
        return pathAndFileName[0]+pathAndFileName[1];
    }
    /**
     * 下载文件
     * */
    public void downloadFile(HttpServletResponse response,String path,String filename){
        InputStream file = getFile(path,filename);
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);

        try(ServletOutputStream servletOutputStream = response.getOutputStream(); ) {
            int len;
            byte[] buffer = new byte[1024];
            while((len=file.read(buffer)) > 0){
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 预览图片.
     *
     * @param path 路径
     * @param fileName 文件名
     * @return url 预览文件url
     */
    //todo 好像没用？？？
    public String previewFile(String path,String fileName){
        // 查看文件地址
        GetPresignedObjectUrlArgs build =
                GetPresignedObjectUrlArgs.builder()
                        .bucket(prop.getBucketName())
                        .object(path+fileName)
                        .method(Method.GET)
                        .build();
        try {
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构造聊天图片记录路径和文件名
     *
     * */
    private String[] createChatImgPathAndFileName(int goodId,int fromUserId,long time){
        String[] pathAndFileName=new String[2];
        pathAndFileName[0]=PATH_CHATS+"/"+goodId+"/";
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
    private void putFile(MultipartFile file,String path,String fileName) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(prop.getBucketName())
                            .object(path+fileName)
                            .stream(file.getInputStream(), file.getSize(),-1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
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
     * 删除单个文件
     *
     * @param filename 文件名
     * @param path 储存路径
     * */
    private void removeFile(String filename,String path) {
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
     *
     * @param files 文件：
     *              <code>Map(文件路径,文件名)</code>
     *
     * */
    private void removeFile(Map<String,String> files) {
        //todo 用bean传参！！！！！！！！！！！！！！！！！！！
        List<DeleteObject> objects = new LinkedList<>();
        for (Map.Entry<String,String> entry:files.entrySet()){
            objects.add(new DeleteObject(entry.getKey()+entry.getValue()));
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
     * 获取单个文件
     *
     * @param path 储存路径
     * @param fileName 文件名
     * */
    private InputStream getFile(String path, String fileName) {
        try (
                InputStream inputStream=minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(prop.getBucketName())
                                .object(path+fileName)
                                .build()
                )
        ){
            return inputStream;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
