package com.xing.chatroomapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xing.chatroomapi.pojo.entity.Group;
import com.xing.chatroomapi.pojo.entity.User;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/5/7
 */
public interface GroupService extends IService<Group> {

    /**
     * @description: 创建一个群聊
     * @param: [java.lang.String, java.lang.String, java.util.List<java.lang.Integer>]
     * @return: void
     */
    void createGroup(String groupName, String groupInfo, List<Integer> memberIds);
}
