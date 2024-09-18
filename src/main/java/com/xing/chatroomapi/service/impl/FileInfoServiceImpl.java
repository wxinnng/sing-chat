package com.xing.chatroomapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.enums.FileCategoryEnums;
import com.xing.chatroomapi.enums.FileDelFlagEnums;
import com.xing.chatroomapi.enums.UploadStatus;
import com.xing.chatroomapi.exception.BusinessException;
import com.xing.chatroomapi.exception.GeneralException;
import com.xing.chatroomapi.mapper.FileInfoMapper;
import com.xing.chatroomapi.pojo.dto.FileDTO;
import com.xing.chatroomapi.pojo.entity.FileInfo;
import com.xing.chatroomapi.pojo.vo.FileVO;
import com.xing.chatroomapi.service.FileInfoService;
import com.xing.chatroomapi.util.BaseContext;
import com.xing.chatroomapi.util.MinioUtils;
import com.xing.chatroomapi.util.RedisUtil;
import com.xing.chatroomapi.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Author:WangXing
 * @DATE:2024/6/2
 */
@Service
@Slf4j
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {


    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    /**
     * 上传文件方法。
     * 该方法负责检查文件是否已存在，如果存在，则返回已存在标志；如果不存在且是完整文件，则上传文件到MinIO并保存文件信息到数据库。
     *
     * @param fileVO 文件相关信息VO，包含文件本身、MD5、文件名等。
     * @return 如果文件已存在，返回秒传状态码；如果文件上传完成，返回上传完成状态码；否则返回null。
     * @throws GeneralException 如果文件为空，抛出通用异常。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)  //所有的操作都在一个事务里面。
    public HashMap<Object, Object> uploadFile(FileVO fileVO) throws ExecutionException, InterruptedException {

        //判断文件是否为空
        if(fileVO.getFile().isEmpty())
            throw  new GeneralException("文件上传异常");

        FileInfo insertItem = new FileInfo();
        Date now = new Date();
        HashMap<Object, Object> map = new HashMap<>();

        //第一片文件
        if(fileVO.getChunkNumber() == 1){
            //先去数据库看看有没有这个文件
            QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("file_md5", fileVO.getFileMd5());

            //通过Md5查询，别人是不是已经传过这个文件了(文件名不影响文件的MD5值)。
            List<FileInfo> fileInfoList = fileInfoMapper.selectList(queryWrapper);
            FileInfo fileInfo = null;
            if(fileInfoList.size() > 0){
                fileInfo = fileInfoList.get(0);
            }
            //别人已经上传过这个文件了，直接秒传
            if(fileInfo != null){
                log.info("服务器中有相同的文件，直接秒传");
                //说明minIO中有对应的文件
                insertItem.setUserId(fileVO.getUserId());
                insertItem.setFileMd5(fileVO.getFileMd5());
                insertItem.setFileName(fileInfo.getFileName());
                insertItem.setFileCategory(fileInfo.getFileCategory());
                insertItem.setFileId(StringUtil.getRandomString(10));
                insertItem.setDelFlag(FileDelFlagEnums.USING.getFlag());
                insertItem.setFilePid(fileVO.getFilePid());
                insertItem.setFilePath(fileInfo.getFilePath());
                insertItem.setCreateTime(now);
                insertItem.setFileSize(fileInfo.getFileSize());
                insertItem.setState(UploadStatus.UPLOAD_FINISH.getStatus());


                fileInfoMapper.insert(insertItem);
                System.err.println(insertItem);

                map.put("status",UploadStatus.UPLOAD_FINISH.getStatus());
                map.put("fileId",insertItem.getFileId());
                return map;
            }
            //插入 一个切片
            redisUtil.set(fileVO.getFileMd5(),0);
        }

        //去Redis中找这片文件是否已经上传到了Redis中了
        if(Integer.parseInt(redisUtil.get(fileVO.getFileMd5()).toString()) >= fileVO.getChunkNumber()){
            //说明这片文件已经上传过了。
            map.put("status",UploadStatus.UPLOADING.getStatus());
            return map;
        }

        //只有一段，直接放到服务器就行
        if(fileVO.getChunkTotal() == 1){
            int lastDotIndex = fileVO.getFileName().lastIndexOf(".");
            String type = fileVO.getFileName().substring(lastDotIndex + 1);

            //上传文件到MinIo,上传完毕后，保存文件信息到数据库。
            CompletableFuture<String> asyncUploadFileResult = CompletableFuture.supplyAsync(() -> {
                try {
                    return minioUtils.uploadFile(MessageConstant.MINIO_BUCKET, fileVO.getFileName(), fileVO.getFile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }, executor).whenCompleteAsync((result,exception) -> {
                if(exception != null){
                    log.error("上传文件失败",exception);
                }else{

                    // result ===>
                    insertItem.setUserId(fileVO.getUserId());
                    insertItem.setFileMd5(fileVO.getFileMd5());
                    insertItem.setFileName(fileVO.getFileName());
                    insertItem.setFileCategory(FileCategoryEnums.getByCode(type).getCategory());
                    insertItem.setFileId(StringUtil.getRandomString(10));
                    insertItem.setDelFlag(FileDelFlagEnums.USING.getFlag());
                    insertItem.setFilePid(fileVO.getFilePid());
                    insertItem.setFilePath(result);
                    insertItem.setCreateTime(now);
                    insertItem.setFileSize(fileVO.getFile().getSize());
                    insertItem.setState(UploadStatus.UPLOAD_FINISH.getStatus());

                    fileInfoMapper.insert(insertItem);
                }
            });

            //删除redis中的切片上传信息
            redisUtil.del(fileVO.getFileMd5());
            map.put("status",UploadStatus.UPLOAD_FINISH.getStatus());
            map.put("fileId",insertItem.getFileId());

            //返回结果
            return map;
        }

        log.info("分片上传====> md5 :{} ,=====> index :{}",fileVO.getFileMd5(),fileVO.getChunkNumber());
        //不止一片，继续上传
        //放切片文件的目录是 文件的userId + md5值,这个是唯一的。
        String objectName = fileVO.getUserId() + fileVO.getFileMd5() ;

        try {
            minioUtils.putChunkObject(fileVO.getFile().getInputStream(), MessageConstant.MINIO_BUCKET, objectName + "/" + fileVO.getChunkNumber());
        } catch (IOException e) {
            throw new GeneralException("文件上传异常！");
        }

        //最后一片，进行合并
        if(Objects.equals(fileVO.getChunkNumber(), fileVO.getChunkTotal())){

            //获得文件类型
            int lastDotIndex = fileVO.getFileName().lastIndexOf(".");
            String type = fileVO.getFileName().substring(lastDotIndex + 1);

            //objectName : userId+md5
            String filePath = minioUtils.composeObject(MessageConstant.MINIO_BUCKET,MessageConstant.MINIO_BUCKET,objectName, type);


            insertItem.setUserId(fileVO.getUserId());
            insertItem.setFileMd5(fileVO.getFileMd5());
            insertItem.setFileName(fileVO.getFileName());
            insertItem.setFileCategory(FileCategoryEnums.getByCode(type).getCategory());
            insertItem.setFileId(StringUtil.getRandomString(10));
            insertItem.setDelFlag(FileDelFlagEnums.USING.getFlag());
            insertItem.setFilePid(fileVO.getFilePid());
            insertItem.setFilePath(filePath);
            insertItem.setCreateTime(now);
            Long fileSize = MessageConstant.DEFAULT_CHUNK_SIZE * (fileVO.getChunkTotal() - 1) + fileVO.getFile().getSize();
            insertItem.setFileSize(fileSize);
            insertItem.setState(UploadStatus.UPLOAD_FINISH.getStatus());

            //插入一条数据
            System.out.println(fileInfoMapper.insert(insertItem));

            //删除minio中的临时文件目录
            System.out.println(minioUtils.deleteFolder(MessageConstant.MINIO_BUCKET, objectName));

            //删除redis中的切片上传信息
            redisUtil.del(fileVO.getFileMd5());
            map.put("status",UploadStatus.UPLOAD_FINISH.getStatus());
            map.put("fileId",insertItem.getFileId());

            //返回结果
            return map;
        }

        //更新redis中的切片上传信息
        redisUtil.incrby(fileVO.getFileMd5(),1);
        //上传中
        map.put("status",UploadStatus.UPLOADING.getStatus());
        //返回结果
        return map;
    }

    /**
     * 创建文件夹。
     *
     * @param folderName 文件夹名称。
     * @param pid 父文件夹ID，如果为null，则默认为"root"。
     */
    @Override
    public void createFolder(String folderName,String pid) {

        //获取当前用户
        Integer userId = BaseContext.getCurrentUser();

        if(pid == null)
            pid = "root";

        //创建的文件目录，传过来的不会是重的，前端做校验
        Date now = new Date();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(StringUtil.getRandomString(10));
        fileInfo.setFilePid(pid);
        fileInfo.setFileCategory(FileCategoryEnums.FOLDER.getCategory());
        fileInfo.setCreateTime(now);
        fileInfo.setFileName(folderName);
        fileInfo.setUserId(userId);
        fileInfo.setDelFlag(FileDelFlagEnums.USING.getFlag());

        //插入到数据库中
        fileInfoMapper.insert(fileInfo);
    }

    @Override
    public List<FileInfo> loadRecycleFileList() {
        return fileInfoMapper.getRecycleList(BaseContext.getCurrentUser());
    }

    @Override
    public void saveFile(String fileId) throws BusinessException {
        //先去查
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if(fileInfo == null)
            throw new BusinessException("参数错误！");

        Date now = new Date();
        fileInfo.setFileId(StringUtil.getRandomString(10));
        fileInfo.setFilePid("root");
        fileInfo.setUserId(BaseContext.getCurrentUser());
        fileInfo.setCreateTime(now);

        //插入到数据库
        fileInfoMapper.insert(fileInfo);

    }

    @Override
    public String getFileId(String fileMd5, String filePid) {
        if(filePid == null)
            filePid = "root";
        String[] filePid1 = fileInfoMapper.getFilePid(fileMd5, filePid);
        return filePid1[0];
    }

    @Override
    public void changeFileStatus(String fileId, Integer type) {
        //0:删除  1：回收 2：删除
        FileInfo fileInfo = new FileInfo();
        Integer userId = BaseContext.getCurrentUser();
        fileInfo.setFileId(fileId);
        fileInfo.setUserId(userId);
        fileInfo.setDelFlag(type);
        fileInfoMapper.updateFileInfoByFileId(fileInfo);
    }

    /**
     * 根据项目ID加载文件列表。
     * 此方法根据当前用户和指定的项目ID（如果提供），从数据库中检索文件信息，并按创建时间分组。
     * 如果未指定项目ID，则默认查询根项目下的文件。
     *
     * @param pid 项目ID，用于筛选文件。如果为null，则查询根项目下的文件。
     * @return 返回一个映射，其中键是文件的创建时间（以毫秒为单位的时间戳），值是该时间点创建的文件列表。
     */
    @Override
    public Map<Long, List<FileDTO>> loadFileList(String pid) {
        Integer user = BaseContext.getCurrentUser();

        if(pid == null)
            pid = "root";

        return fileInfoMapper.selectByUserIdAndPidGroup(user, pid).stream().collect(Collectors.groupingBy(
                obj -> obj.getCreateTime().getTime(),
                Collectors.toList()
        ));
    }

}
