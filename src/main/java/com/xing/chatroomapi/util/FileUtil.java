package com.xing.chatroomapi.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Author:WangXing
 * @DATE:2024/5/9
 */
public class FileUtil {
    //把文件MultipartFile转为File
    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
