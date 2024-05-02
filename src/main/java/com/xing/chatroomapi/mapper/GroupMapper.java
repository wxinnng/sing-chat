package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/4/30
 */
@SuppressWarnings("all")
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    @Update("update group where id = #{groupId} set curNum = curNum + 1")
    void incrCurNum(Integer groupId);

    List<Group> getGroupRelation(Integer currentUser);
}
