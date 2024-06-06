package com.xing.chatroomapi.controller;

import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.exception.GeneralException;
import com.xing.chatroomapi.pojo.vo.FileVO;
import com.xing.chatroomapi.pojo.vo.ResultJson;
import com.xing.chatroomapi.service.FileInfoService;
import com.xing.chatroomapi.util.MinioUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Author:WangXing
 * @DATE:2024/6/4
 */
@RestController
@RequestMapping("/file")
@Slf4j
@Api("文件相关接口")
@CrossOrigin
public class FileController {

    @Resource
    private MinioUtils minioUtils;

    @Autowired
    private FileInfoService fileInfoService;

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public ResultJson uploadFile(
            @RequestParam Integer chunkNumber,
            @RequestParam Integer chunkTotal,
            @RequestParam MultipartFile file,
            @RequestParam String fileName,
            @RequestParam String fileMd5,
            @RequestParam String filePid,
            @RequestParam Integer userId
    ) {
        log.info("上传文件");
        FileVO form = new FileVO(userId,file, chunkNumber, chunkTotal, fileName,fileMd5,filePid);

        System.err.println(form);
        if(form.getFile() == null)
            return ResultJson.error("文件信息错误",MessageConstant.FILE_UPLOAD_ERROR);
//        try{
        ;
        return ResultJson.success(fileInfoService.uploadFile(form));
//        }catch (GeneralException e){
//            return ResultJson.error("文件上传失败", MessageConstant.FILE_UPLOAD_ERROR);
//        }catch (Exception e){
//            return ResultJson.error("服务器异常",MessageConstant.SERVER_ERROR);
//        }
    }


    @PostMapping("/c_folder")
    @ApiOperation("创建文件夹")
    public ResultJson createFolder(@RequestParam String folderName,@RequestParam(required = false) String pid){
        log.info("创建文件夹{}",folderName);
        fileInfoService.createFolder(folderName,pid);
        return ResultJson.success();
    }

    @GetMapping("/list")
    @ApiOperation("获得文件列表")
    public ResultJson getFileList(@RequestParam(required = false) String pid){
        log.info("获得文件列表 {}",pid);  //pid可以是null
        return ResultJson.success(fileInfoService.loadFileList(pid));
    }

}
