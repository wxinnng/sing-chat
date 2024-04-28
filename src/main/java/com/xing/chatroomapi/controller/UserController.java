package com.xing.chatroomapi.controller;

import com.xing.chatroomapi.pojo.entity.User;
import com.xing.chatroomapi.pojo.vo.ResultJson;
import com.xing.chatroomapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/4/26
 */
@Controller
@Api(value = "用户接口")
@RestController("/user")
public class UserController {

    //注入用户服务
    @Autowired
    private UserService userService;


    /**
     * @description: 用户登录
     * @param: []
     * @return: com.xing.chatroomapi.pojo.vo.ResultJson<java.util.List<com.xing.chatroomapi.pojo.entity.User>>
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResultJson<List<User>> getUserList(){
        return null;
    }



}
