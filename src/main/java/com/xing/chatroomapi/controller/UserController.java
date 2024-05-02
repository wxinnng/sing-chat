package com.xing.chatroomapi.controller;

import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.exception.BusinessException;
import com.xing.chatroomapi.pojo.ChatSession;
import com.xing.chatroomapi.pojo.Relation;
import com.xing.chatroomapi.pojo.vo.ResultJson;
import com.xing.chatroomapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author:WangXing
 * @DATE:2024/4/26
 */
@Controller
@Slf4j
@Api(value = "用户接口")
@RestController
@RequestMapping("/user")
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
    public ResultJson register(@RequestParam String nickName,@RequestParam String password){
        log.info("用户注册 {}",nickName);
        try{
            return ResultJson.success(userService.register(nickName,password));
        }catch (BusinessException e){
            return ResultJson.error(e.getMessage(), MessageConstant.REGISTER_ERROR);
        }catch (Exception e){
            return ResultJson.error("服务器异常!",MessageConstant.SERVER_ERROR);
        }
    }

    /**
     * @description: 查询用户
     * @param: [java.lang.Integer]
     * @return: com.xing.chatroomapi.pojo.vo.ResultJson
     */
    @ApiOperation("查询用户")
    @GetMapping("/search")
    public ResultJson searchUser(@RequestParam Integer id){
        log.info("查找用户 {}",id);
        try{
            return ResultJson.success(userService.getUserInfoById(id));
        }catch (Exception e){
            return ResultJson.error("服务器异常！",MessageConstant.SERVER_ERROR);
        }
    }

    /**
     * @description: 发送申请
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: com.xing.chatroomapi.pojo.vo.ResultJson
     */
    @ApiOperation("发送申请")
    @PostMapping("/postApplication")
    public ResultJson postApplication(@RequestParam Integer toTarget,@RequestParam Integer type){
        log.info("向{} : {}发送申请",type,toTarget);
        try {
            userService.postApplication(toTarget,type);
            return ResultJson.success("请求发送成功！");
        } catch (BusinessException e) {
            return ResultJson.error(e.getMessage(),MessageConstant.TARGET_NOT_FOUND);
        }
    }

    /**
     * @description:加载信息
     * @param: []
     * @return: com.xing.chatroomapi.pojo.vo.ResultJson
     */
    @ApiOperation("加载信息")
    @GetMapping("/message")
    public ResultJson loadMessage(){
        log.info("加载message");
        return ResultJson.success(userService.loadMessage());
    }


    /**
     * @description: 处理请求
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: com.xing.chatroomapi.pojo.vo.ResultJson
     */
    @ApiOperation("处理申请")
    @GetMapping("/operateApplication")
    public ResultJson operateApplication(Integer applicationId,Integer type,Integer groupType){
        log.info("处理申请");
        try{
            userService.operateApplication(applicationId,type,groupType);
            return ResultJson.success();
        }catch (BusinessException e){
            return ResultJson.error(e.getMessage(),MessageConstant.PARAMS_ERROR);
        }
    }

    /**
     * @description: 加载用户Relations
     * @param: []
     * @return: com.xing.chatroomapi.pojo.vo.ResultJson
     */
    @ApiOperation("加载用户好友列表")
    @GetMapping("/loadRelations")
    public ResultJson loadUserRelationList(){
        log.info("加载用户列表");
        try {
            List<ChatSession> relations = userService.loadRelationList();
            return ResultJson.success(relations);
        } catch (BusinessException e) {
            return ResultJson.error("身份认证失败！",MessageConstant.USER_INFO_ERROR);
        }
    }
}
