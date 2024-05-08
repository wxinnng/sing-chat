package com.xing.chatroomapi.controller;


import com.xing.chatroomapi.pojo.vo.CreateGroupVO;
import com.xing.chatroomapi.pojo.vo.ResultJson;
import com.xing.chatroomapi.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/5/7
 */
@RequestMapping("/group")
@RestController
@Api("群聊接口")
@Slf4j
public class GroupController {


    @Autowired
    private GroupService groupService;

    /**
     * @description: 创建一个群聊
     * @param: [java.lang.String, java.lang.String, java.util.List<java.lang.Integer>]
     * @return: com.xing.chatroomapi.pojo.vo.ResultJson
     */
    @ApiOperation("创建群聊")
    @PostMapping("/create")
    public ResultJson createGroup(@RequestBody CreateGroupVO createGroupVO){
        log.info("创建群聊");
        groupService.createGroup(createGroupVO.getGroupName(), createGroupVO.getGroupInfo(), createGroupVO.getMemberIds());
        return ResultJson.success();
    }

}
