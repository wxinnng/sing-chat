package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.ChatSession;
import com.xing.chatroomapi.pojo.entity.User;
import com.xing.chatroomapi.pojo.entity.UserRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/5/1
 */
@Mapper
@SuppressWarnings("all")
public interface UserRelationMapper extends BaseMapper<UserRelation> {
    List<User> getUserRelationList(Integer currentUser);

    @Select("select * from user_relation where user_one = #{currentUser} and user_two = #{toTarget}")
    UserRelation selectByUsers(Integer currentUser, Integer toTarget);
}
