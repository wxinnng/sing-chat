package com.xing.chatroomapi.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author:WangXing
 * @DATE:2024/6/2
 */
@Data
@TableName(value = "file_info")
public class FileInfo {
    private String fileId;
    private Integer userId;
    private String fileMd5;
    private String filePid;
    private Long fileSize;
    private String fileName;
    private String filePath;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer fileCategory;
    private Integer state;
    private Integer delFlag;
}
