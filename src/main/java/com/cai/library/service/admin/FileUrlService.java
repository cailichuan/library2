package com.cai.library.service.admin;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileUrlService {

    /** 头像文件大小的上限值(1MB) */
    public static final int AVATAR_MAX_SIZE = 1 * 1024 * 1024;
    /** 允许上传的头像的文件类型 */
    public static final List<String> AVATAR_TYPES = new ArrayList<String>();
    /** 初始化允许上传的头像的文件类型 */
    static {
        AVATAR_TYPES.add("image/jpeg");
        AVATAR_TYPES.add("image/jpg");
        AVATAR_TYPES.add("image/png");
        AVATAR_TYPES.add("image/bmp");
        AVATAR_TYPES.add("image/gif");
        AVATAR_TYPES.add("image/pjpeg");
        AVATAR_TYPES.add("image/x-png");

    }

    public Map<Boolean,String> saveImg(MultipartFile file,String bookId) throws FileNotFoundException {
        //创建返回结果的map
        Map<Boolean,String> resultMap=new HashMap<Boolean,String>(1);

        //判断上传的文件是否为空
        if (file.isEmpty()) {
            resultMap.put(false,"上传的文件为空");
            return resultMap;
        }
        //判断上传的文件大小是否超出限制
        if (file.getSize()>AVATAR_MAX_SIZE){
            resultMap.put(false,"上传的文件大于"+(AVATAR_MAX_SIZE/1024)+"KB");
            return resultMap;
        }
        //判断上传的文件类型是否超出限制
        String contentType=file.getContentType();
        if (!AVATAR_TYPES.contains(contentType)){
            resultMap.put(false,"上传的文件类型错误");
            return resultMap;
        }


//这个是打包后的路径
//        获取jar包所在的目录
        ApplicationHome homePath = new ApplicationHome(getClass());
        File jarF = homePath.getSource();
        //在jar包目录下生成一个upload文件夹用来存储上传的图片
        String parent = jarF.getParentFile().toString() + "/classes/static/images";



//这个是打包前路径
//        String parent = System.getProperty("user.dir") + "/src/main/resources/static/images";
//        System.out.println(parent);


        //保存图片的文件夹
        File dir = new File(parent);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //保存图片文件的文件名
        String suffix="";
        //获取上传时的文件名
        String originalFilename = file.getOriginalFilename();
        //获取格式后缀
        int beginIndex = originalFilename.lastIndexOf(".");
        if(beginIndex>0){
            suffix=originalFilename.substring(beginIndex);
        }

        //新的文件名

        String filename = bookId + suffix;
        System.out.println(filename);

        //创建文件对象
        File dest=new File(dir,filename);

        //执行保存头像文件
        try {
            file.transferTo(dest);
            //把文件类型存入数据库中

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //图片路径
        String filePath = "/images/" + filename;

        resultMap.put(true,filename);

        return resultMap;


    }
}
