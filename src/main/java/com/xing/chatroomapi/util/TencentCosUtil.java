package com.xing.chatroomapi.util;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.xing.chatroomapi.constant.MessageConstant;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Random;

/**
 * 腾讯云对象存储工具类
 *
 */public class TencentCosUtil {

    //bucket的区域
    private static final String region = MessageConstant.REGION;
    // 此处填写的存储桶名称
    private static final String bucketName = MessageConstant.BUCKET;
    // secretId
    private static final String secretId = MessageConstant.SECRET_ID;
    // secretKey
    private static final String secretKey = MessageConstant.SECRET_KEY;

    private static final String prefix = "/avatar/";

    //
    private static final String URL = "https://singingchat-1322885130.cos.ap-beijing.myqcloud.com";

    // 1 初始化用户身份信息(secretId, secretKey，可在腾讯云后台中的API密钥管理中查看！
    private final static COSCredentials credentials = new BasicCOSCredentials(secretId,secretKey);

    // 2 设置bucket的区域, COS地域的简称请参照
    private final static ClientConfig clientConfig = new ClientConfig(new Region(region));


    public static String uploadfile(MultipartFile file){
        // 创建 COS 客户端连接
        COSClient cosClient = new COSClient(credentials,clientConfig);
        String fileName = file.getOriginalFilename();
        try {
            assert fileName != null;
            String substring = fileName.substring(fileName.lastIndexOf("."));
            File localFile = File.createTempFile(String.valueOf(System.currentTimeMillis()),substring);
            file.transferTo(localFile);
            Random random = new Random();
            fileName =prefix + random.nextInt(10000)+System.currentTimeMillis()+substring;
            // 将 文件上传至 COS
            PutObjectRequest objectRequest = new PutObjectRequest(bucketName,fileName,localFile);
            cosClient.putObject(objectRequest);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }finally {
            cosClient.shutdown();
        }
        return URL+fileName;
    }


}
