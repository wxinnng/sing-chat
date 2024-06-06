package com.xing.chatroomapi.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author:WangXing
 * @DATE:2024/6/4
 */
@Data
@AllArgsConstructor
public class FileVO {
    private Integer userId;
    private MultipartFile file;
    private Integer chunkNumber;
    private Integer chunkTotal;
    private String fileName;
    private String fileMd5;
    private String filePid;
}
