package com.xing.chatroomapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xing.chatroomapi.pojo.dto.FileDTO;
import com.xing.chatroomapi.pojo.entity.FileInfo;
import com.xing.chatroomapi.pojo.vo.FileVO;

import java.util.List;
import java.util.Map;

/**
 * @Author:WangXing
 * @DATE:2024/6/2
 */
public interface FileInfoService extends IService<FileInfo> {


    Integer uploadFile(FileVO fileVO);

    void createFolder(String folderName,String pid);

    Map<Long, List<FileDTO>> loadFileList(String pid);
}
