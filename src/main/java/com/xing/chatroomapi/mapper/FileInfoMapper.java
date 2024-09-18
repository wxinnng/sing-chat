package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.dto.FileDTO;
import com.xing.chatroomapi.pojo.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/6/2
 */
@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {
    List<FileDTO> selectByUserIdAndPidGroup(Integer user, String pid);

    void updateFileInfoByFileId(FileInfo fileInfo);

    Long getUserSpace(Integer user);

    List<FileInfo> getRecycleList(Integer currentUser);

    @Select("select file_id from file_info where file_md5 = #{fileMd5} and file_pid = #{filePid}" )
    String[] getFilePid(String fileMd5, String filePid);
}
