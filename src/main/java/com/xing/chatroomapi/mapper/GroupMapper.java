package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.entity.Group;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/4/30
 */

@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    @Update("update group_info  set cur_num = cur_num + 1 where id = #{groupId}")
    void incrCurNum(Integer groupId);

    List<Group> getGroupRelation(Integer currentUser);

    Group getGroupRelationDetail(Integer id,Integer userId);

    @Update("update group_info set cur_num = cur_num - 1 where id = #{id}")
    void deCurNum(Integer id);
}
