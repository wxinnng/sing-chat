package com.xing.chatroomapi.controller;

import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.enums.FileDelFlagEnums;
import com.xing.chatroomapi.exception.BusinessException;
import com.xing.chatroomapi.pojo.vo.FileVO;
import com.xing.chatroomapi.pojo.vo.ResultJson;
import com.xing.chatroomapi.service.FileInfoService;
import com.xing.chatroomapi.util.MinioUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
            return ResultJson.success(fileInfoService.uploadFile(form));
//        }catch (GeneralException e){
//            return ResultJson.error("文件上传失败", UploadStatus.UPLOAD_FAIL.getStatus());
//        }catch (Exception e){
//            return ResultJson.error("服务器异常",UploadStatus.UPLOAD_FAIL.getStatus());
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
        log.info("获得文件列表 {}",pid);
        return ResultJson.success(fileInfoService.loadFileList(pid));
    }

    @GetMapping("/recycle_list")
    @ApiOperation("获得回收站文件列表")
    public ResultJson getRecycleList(){
        log.info("获得回收站文件列表");
        return ResultJson.success(fileInfoService.loadRecycleFileList());
    }

    @GetMapping("/delete")
    @ApiOperation("永久删除")
    public ResultJson deleteFile(@RequestParam String fileId){
        log.info("删除文件 {}",fileId);
        fileInfoService.changeFileStatus(fileId, FileDelFlagEnums.DEL.getFlag());
        return ResultJson.success();
    }

    @GetMapping("/recycle")
    @ApiOperation("放入回收站")
    public ResultJson recycleFile(@RequestParam String fileId){
        log.info("放入回收站 {}",fileId);
        fileInfoService.changeFileStatus(fileId, FileDelFlagEnums.RECYCLE.getFlag());
        return ResultJson.success();
    }

    @GetMapping("/restore")
    @ApiOperation("恢复文件")
    public ResultJson restoreFile(@RequestParam String fileId){
        log.info("恢复文件 {}",fileId);
        fileInfoService.changeFileStatus(fileId, FileDelFlagEnums.USING.getFlag());
        return ResultJson.success();
    }

    @GetMapping("/getFileId/{fileMd5}")
    @ApiOperation("获得文件的filePid")
    public ResultJson getFileId(@PathVariable("fileMd5") String fileMd5,@RequestParam(value = "filePid",required = false) String filePid){
        log.info("获得文件的filePid {}",fileMd5);
        return ResultJson.success(fileInfoService.getFileId(fileMd5,filePid));
    }

    @GetMapping("/save/{fileId}")
    @ApiOperation("保存文件")
    public ResultJson saveFile(@PathVariable("fileId") String fileId){
        log.info("保存文件到root下");
        try {
            fileInfoService.saveFile(fileId);
        } catch (BusinessException e) {
            return ResultJson.error(e.getMessage(),MessageConstant.PARAMS_ERROR);
        }
        return ResultJson.success();

    }

}
