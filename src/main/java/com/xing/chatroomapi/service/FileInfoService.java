package com.xing.chatroomapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xing.chatroomapi.exception.BusinessException;
import com.xing.chatroomapi.pojo.dto.FileDTO;
import com.xing.chatroomapi.pojo.entity.FileInfo;
import com.xing.chatroomapi.pojo.vo.FileVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author:WangXing
 * @DATE:2024/6/2
 */
public interface FileInfoService extends IService<FileInfo> {


    HashMap<Object, Object> uploadFile(FileVO fileVO) throws ExecutionException, InterruptedException;

    void createFolder(String folderName,String pid);

    Map<Long, List<FileDTO>> loadFileList(String pid);

    void changeFileStatus(String fileId, Integer type);

    List<FileInfo> loadRecycleFileList();

    String getFileId(String fileMd5,String filePid);

    void saveFile(String fileId) throws BusinessException;
}
