package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.Relation;
import com.xing.chatroomapi.pojo.dto.CreateGroupUserDTO;
import com.xing.chatroomapi.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/4/26
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User getUserRelationDetail(Integer id,Integer localId);

    void updateUserInfo(User user);

    @Select("select id,nick_name,avatar from user where id = #{id}")
    CreateGroupUserDTO getUserBaseInfo(Integer id);

    List<CreateGroupUserDTO> getUserBaseInfoBatch(List<Integer> ids);
}
