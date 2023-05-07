package com.gseek.gseek0_0.utils;

import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface MultipartFile  extends InputStreamSource {
//    //getName() 返回参数的名称
//    String getName();
//    @Nullable
//    String getOriginalFilename();
//    //getContentType()返回文件的内容类型
//    @Nullable
//    String getContentType();
//    //isEmpty()判断是否为空，或者上传的文件是否有内容
//    boolean isEmpty();
//    long getSize();
//    byte[] getBytes() throws IOException;
//    InputStream getInputStream() throws IOException;
//    default Resource getResource(){
//        return new MultipartFileRescource(this);
//    }
//    void  transferTo(File var1) throws IOException,IllegalStateException;
//    default void transferTo(Path dest) throws IOException,IllegalStateException{
//        FileCopyUtils.copy(this.getInputStream(), Files.newOutputStream(dest));
//    }

}
