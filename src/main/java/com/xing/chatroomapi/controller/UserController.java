package com.xing.chatroomapi.controller;

import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.exception.BusinessException;
import com.xing.chatroomapi.pojo.vo.ResultJson;
import com.xing.chatroomapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author:WangXing
 * @DATE:2024/4/26
 */
@Controller
@Slf4j
@Api(value = "用户接口")
@RestController("/user")
@SuppressWarnings("all")
public class UserController extends BaseController{

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
    public ResultJson login(@RequestParam Integer userId,@RequestParam String password){
        log.info("用户登录 {}",userId);
        try {
            return ResultJson.success(userService.login(userId,password));
        } catch (BusinessException e) {
            return ResultJson.error("账号或密码错误",1001);
        }
    }

    /**
     * @description: 用户注册
     * @param: [java.lang.String, java.lang.String]
     * @return: com.xing.chatroomapi.pojo.vo.ResultJson
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResultJson register(String nickName,String password){
        log.info("用户注册 {}",nickName);
        try{
            return ResultJson.success(userService.register(nickName,password));
        }catch (BusinessException e){
            return ResultJson.error(e.getMessage(), MessageConstant.REGISTER_ERROR);
        }catch (Exception e){
            return ResultJson.error("服务器异常!",MessageConstant.SERVER_ERROR);
        }
    }



}
