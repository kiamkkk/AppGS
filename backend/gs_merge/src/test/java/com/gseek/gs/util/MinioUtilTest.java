package com.gseek.gs.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Phak
 * @since 2023/6/1-16:28
 */
@SpringBootTest
@Slf4j
public class MinioUtilTest {

    /**
     * 测试用例图片路径
     * @deprecated 你得在自己电脑上找张图片作为测试用例.
     * */
    private final static String TEST_IMG_PATH = "C:\\Users\\HP\\Desktop\\imgs\\114514.jpg";
    @Autowired
    MinioUtil minioUtil;

    @Test
    public void getImgWhenImgExist() throws IOException {
        InputStream inputStream=minioUtil.getFile(MinioUtil.PATH_HEAD_SCULPTURES,"头像.jpg");
        assert inputStream.available() ==0 : "不可用";
    }

    /**
     * @deprecated 因getImg(),不能保证在其他设备上适用.
     * */
    @Disabled
    @Test
    public void saveProfileTest() throws IOException {
        String path = minioUtil.saveProfilePhoto(114514, getImg(TEST_IMG_PATH));
        String fileName=path.substring(path.lastIndexOf("/"));
        assert minioUtil.getFile(MinioUtil.PATH_HEAD_SCULPTURES, fileName).available() ==0 : "上传头像失败" ;
    }
    /**
     * @deprecated 因getImg(),不能保证在其他设备上适用.
     * */
    @Disabled
    @Test
    public void deleteImgWhenExist(){
        String path=MinioUtil.PATH_HEAD_SCULPTURES;
        String fileName="114514.jpg";
        minioUtil.removeFile(fileName, path) ;
        assert ! minioUtil.isFileExist(path, fileName) : "删除失败";

        // 把测试用例补回去
        minioUtil.saveProfilePhoto(
                Integer.parseInt(TEST_IMG_PATH.substring(TEST_IMG_PATH.lastIndexOf("\\")+1, TEST_IMG_PATH.lastIndexOf("."))),
                getImg(TEST_IMG_PATH)
        );
    }
    /**
     *
     * */
    @Disabled
    @Test
    public void deleteImgsWhenExist(){

    }

    /**
     * 测试getImg能否正常运行.
     * @deprecated 因getImg(),不能保证在其他设备上适用.
     * */
    @Disabled
    @Test
    public void testGetImg(){
        assert getImg(TEST_IMG_PATH) != null : "获取失败";
    }
    /**
     * 在本地获取一个图片,并转为MultipartFile
     * @deprecated 这个方法要求传入一个固定的绝对路径,以获取MultipartFile实例,不能保证在其他设备上适用.
     *             涉及到这个方法的测试,都要disable.
     * */
    private MultipartFile getImg(String imgPath){
        File file=new File(imgPath);
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;

        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(),file.getName(),
                    "application/octet-stream",fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

}
