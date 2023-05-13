package com.gseek.gs.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class FileUtils {
    public static String fileUtil(MultipartFile file, HttpServletRequest request){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd/");
        String realPath = request.getSession().getServletContext().getRealPath("/upload");
        String format=sdf.format(new Date());
        String filePath="";
        File folder=new File(realPath+format);
        if(!folder.isDirectory()){
            folder.mkdirs();
            String oldName=file.getOriginalFilename();
            String newName= UUID.randomUUID().toString()+
                    oldName.substring(oldName.lastIndexOf("."),oldName.length());
            try{
                file.transferTo(new File(folder,newName));
                filePath=request.getScheme()+"://"+request.getServerName()+":"+
                        request.getServerPort()+"/uploadFile"+format+newName;
            }catch (IOException e){
                e.printStackTrace();
                return "上传失败";
            }
        }
        System.out.println(realPath);
        return realPath;
    }
}
