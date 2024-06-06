package com.xing.chatroomapi.util;

import com.qcloud.cos.utils.IOUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author:WangXing
 * @DATE:2024/6/5
 */
@Slf4j
@Service
public class MinioService {
    @Autowired
    private MinioClient minioClient;


    private final static String bucketName = "singing";


    /**
     * description: 判断bucket是否存在，不存在则创建
     */
    public void existBucket(String name) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建存储bucket
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据名称删除bucket
     * @Date 2021/9/1 17:17
     * @param
     * @return
     **/
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }




    //上传文件
    public List<String> upload(MultipartFile[] multipartFile) {
        List<String> names = new ArrayList<>(multipartFile.length);
        for (MultipartFile file : multipartFile) {
            String fileName = file.getOriginalFilename();
            String[] split = fileName.split("\\.");
            if (split.length > 1) {
                fileName = split[0] + "_" + System.currentTimeMillis() + "." + split[1];
            } else {
                fileName = fileName + System.currentTimeMillis();
            }
            InputStream in = null;
            try {
                in = file.getInputStream();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(in, in.available(), -1)
                        .contentType(file.getContentType())
                        .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            names.add(fileName);
        }
        return names;
    }

    /**
     * 获取所有的bucket
     * @Date 2021/9/1 17:13
     * @param
     * @return
     **/
    @SneakyThrows
    public List<Bucket> getAllBuckets(){
        return minioClient.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     * @Date 2021/9/1 17:16
     * @param
     * @return
     **/
    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName){
        return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }




    /**
     * @Description: 上传文件（不带文件大小）
     * @Param:
     * @return:
     * @Author: Dufl
     * @Date: 2022/4/20 16:13
     */
    @SneakyThrows  //抛出异常
    public void putObject(String bucketName, String objectName, InputStream stream){
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(stream, -1, 10485760)
                .build();
        minioClient.putObject(putObjectArgs);
        if (stream != null){
            stream.close();
        }
    }

    /**
     * @Description: 上传文件（需要传文件大小及文件类型）
     * @Param:
     * @return:
     * @Author: Dufl
     * @Date: 2022/4/20 16:14
     */
    @SneakyThrows
    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contentType){
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(stream, size, -1)
                .contentType(contentType)
                .build();
        minioClient.putObject(putObjectArgs);
    }

    /**
     * 获取文件外链
     * @Date 2021/9/1 17:22
     * @param
     * @return
     **/
    @SneakyThrows
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires){
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET) //这个不能少
                .bucket(bucketName)
                .object(objectName)
                .expiry(expires)
                .build();

        return minioClient.getPresignedObjectUrl(build);
    }

    /**
     * 获取文件外链
     * @Date 2021/9/1 17:22
     * @param
     * @return
     **/
    @SneakyThrows
    public String getPresignedObjectUrl(String bucketName, String objectName){
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .build();
        return minioClient.getPresignedObjectUrl(build);
    }

    /**
     * @Description: 获取文件
     * @Param:
     * @return:
     * @Author: Dufl
     * @Date: 2022/4/20 16:15
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName){
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * @Description: 获取文件信息
     * @Param:
     * @return:
     * @Author: Dufl
     * @Date: 2022/4/20 16:15
     */
    @SneakyThrows
    public StatObjectResponse getObjectInfo(String bucketName, String objectName){
        return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * @Description: 删除文件
     * @Param:
     * @return:
     * @Author: Dufl
     * @Date: 2022/4/20 16:15
     */
    @SneakyThrows
    public void remove(String bucketName, String objectName){
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }


    /**
     * @Description: 下载文件
     * @Param:
     * @return:
     * @Author: Dufl
     * @Date: 2022/4/20 16:15
     */
    public ResponseEntity<byte[]> download(String fileName) {
        ResponseEntity<byte[]> responseEntity = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            //封装返回值
            byte[] bytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            try {
                headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            headers.setContentLength(bytes.length);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccessControlExposeHeaders(Arrays.asList("*"));
            responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseEntity;
    }




    /**
     * 批量删除文件对象
     * @param bucketName 存储bucket名称
     * @param objects 对象名称集合
     */
    public Iterable<Result<DeleteError>> removeObjects(String bucketName, List<String> objects) {
        List<DeleteObject> dos = objects.stream().map(e -> new DeleteObject(e)).collect(Collectors.toList());
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos).build());
        return results;
    }

}


