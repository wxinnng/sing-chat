package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.dto.FileDTO;
import com.xing.chatroomapi.pojo.entity.FileInfo;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/6/2
 */
public interface FileInfoMapper extends BaseMapper<FileInfo> {
    List<FileDTO> selectByUserIdAndPidGroup(Integer user, String pid);
}
