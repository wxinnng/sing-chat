package com.xing.chatroomapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xing.chatroomapi.exception.BusinessException;
import com.xing.chatroomapi.pojo.entity.User;
import com.xing.chatroomapi.pojo.vo.UserVO;

/**
 * @Author:WangXing
 * @DATE:2024/4/26
 */
public interface UserService extends IService<User> {
    /**
     * @description: 用户登录
     * @param: [java.lang.Integer, java.lang.String]
     * @return: com.xing.chatroomapi.pojo.entity.User
     */
    UserVO login(Integer userId, String password) throws BusinessException;

    /**
     * @description: 用户登录
     * @param: [java.lang.String, java.lang.String]
     * @return: com.xing.chatroomapi.pojo.vo.UserVO
     */
    Integer register(String nickName, String password) throws BusinessException;
}
