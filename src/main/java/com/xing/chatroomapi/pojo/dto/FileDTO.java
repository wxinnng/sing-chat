package com.xing.chatroomapi.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author:WangXing
 * @DATE:2024/6/5
 */
@Data
public class FileDTO {
    private String fileId;
    private String filePid;
    private Integer userId;
    private String fileName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    private Integer fileCategory;
    private String filePath;
    private Integer delFlag;
}
