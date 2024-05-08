package com.xing.chatroomapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xing.chatroomapi.exception.BusinessException;
import com.xing.chatroomapi.pojo.ChatSession;
import com.xing.chatroomapi.pojo.dto.CreateGroupUserDTO;
import com.xing.chatroomapi.pojo.entity.User;
import com.xing.chatroomapi.pojo.vo.ApplicationVO;
import com.xing.chatroomapi.pojo.vo.UserVO;

import java.util.List;

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

    /**
     * @description: 通过ID查找用户
     * @param: [java.lang.Integer]
     * @return: com.xing.chatroomapi.pojo.entity.User
     */
    ChatSession getUserInfoById(Integer id);

    /**
     * @description: 用户对目标发送请求
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: void
     */
    void postApplication(Integer toTarget, Integer type) throws BusinessException;

    /**
     * @description: 获得申请列表
     * @param: []
     * @return: java.util.List<com.xing.chatroomapi.pojo.entity.Application>
     */
    List<ApplicationVO> loadMessage();

    /**
     * @description: 处理请求
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: void
     */
    void operateApplication(Integer applicationId, Integer type,Integer groupType) throws BusinessException;

    /**
     * @description: 加载用户的好友列表
     * @param: []
     * @return: java.util.List<com.xing.chatroomapi.pojo.entity.UserRelation>
     */
    List<ChatSession> loadRelationList() throws BusinessException;

    /**
     * @description: 加载用户创建群聊时，可供选择的好友
     * @param: []
     * @return: java.util.List<com.xing.chatroomapi.pojo.dto.CreateGroupUserDTO>
     */
    List<CreateGroupUserDTO> loadCreateGroupUserList();
}
