package com.xing.chatroomapi.controller;

import com.xing.chatroomapi.pojo.vo.ResultJson;
import com.xing.chatroomapi.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:WangXing
 * @DATE:2024/5/6
 */
@Api("聊天相关接口")
@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * @description: 加载聊天记录
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: com.xing.chatroomapi.pojo.vo.ResultJson
     */
    @PostMapping("/messages")
    @ApiOperation("加载聊天记录")
    public ResultJson loadChatMessages(@RequestParam Integer user1,@RequestParam Integer user2,@RequestParam Integer type){
        log.info("加载聊天记录");
        return ResultJson.success(chatService.getMessageByChatId(user1,user2,type));
    }

}
