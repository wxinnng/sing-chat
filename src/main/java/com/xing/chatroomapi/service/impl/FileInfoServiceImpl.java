package com.xing.chatroomapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.enums.FileCategoryEnums;
import com.xing.chatroomapi.enums.FileDelFlagEnums;
import com.xing.chatroomapi.enums.UploadStatus;
import com.xing.chatroomapi.exception.GeneralException;
import com.xing.chatroomapi.mapper.FileInfoMapper;
import com.xing.chatroomapi.pojo.dto.FileDTO;
import com.xing.chatroomapi.pojo.entity.FileInfo;
import com.xing.chatroomapi.pojo.vo.FileVO;
import com.xing.chatroomapi.util.BaseContext;
import com.xing.chatroomapi.util.MinioUtils;
import com.xing.chatroomapi.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:WangXing
 * @DATE:2024/6/2
 */
@Service
@Slf4j
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements com.xing.chatroomapi.service.FileInfoService {


    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)  //所有的操作都在一个事务里面。
    public Integer uploadFile(FileVO fileVO) {

        if(fileVO.getFile().isEmpty())
            throw  new GeneralException("文件上传异常");

        //先去数据库看看有没有这个文件
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_md5", fileVO.getFileMd5());

        //通过Md5查询，别人是不是已经传过这个文件了(文件名不影响文件的MD5值)。
        FileInfo fileInfo = fileInfoMapper.selectOne(queryWrapper);

        if(fileInfo != null){
            //说明minIO中有对应的文件
            //TODO:更新用户信息

            return UploadStatus.UPLOAD_SECONDS.getStatus();
        }

        if(fileVO.getChunkTotal() == 1){
            //只有一段，直接放到服务器就行

            Date now = new Date();

            int lastDotIndex = fileVO.getFileName().lastIndexOf(".");
            String fileName = fileVO.getFileName().substring(0, lastDotIndex);
            String type = fileVO.getFileName().substring(lastDotIndex + 1);

            String url = minioUtils.uploadFile(MessageConstant.MINIO_BUCKET,fileVO.getFileName(), fileVO.getFile());
            FileInfo insertItem = new FileInfo();
            insertItem.setUserId(fileVO.getUserId());
            insertItem.setFileMd5(fileVO.getFileMd5());
            insertItem.setFileName(fileName);
            insertItem.setFileCategory(FileCategoryEnums.getByCode(type).getCategory());
            insertItem.setFileId(StringUtil.getRandomString(10));
            insertItem.setDelFlag(FileDelFlagEnums.USING.getFlag());
            insertItem.setFilePid(fileVO.getFilePid());
            insertItem.setFilePath(url);
            insertItem.setCreateTime(now);
            insertItem.setFileSize(fileVO.getFile().getSize());

            System.err.println(insertItem);

            fileInfoMapper.insert(insertItem);
            return UploadStatus.UPLOAD_FINISH.getStatus();
        }
        return null;
    }


    @Override
    public void createFolder(String folderName,String pid) {

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
    public Map<Long, List<FileDTO>> loadFileList(String pid) {
        Integer user = BaseContext.getCurrentUser();
        System.err.println(user);
        if(pid == null)
            pid = "root";

        return fileInfoMapper.selectByUserIdAndPidGroup(user, pid).stream().collect(Collectors.groupingBy(
                obj -> obj.getCreateTime().getTime(),
                Collectors.toList()
        ));
    }

}
