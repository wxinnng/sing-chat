package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.ChatSession;
import com.xing.chatroomapi.pojo.entity.User;
import com.xing.chatroomapi.pojo.entity.UserRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/5/1
 */
@Mapper
public interface UserRelationMapper extends BaseMapper<UserRelation> {
    List<User> getUserRelationList(Integer currentUser);
}
