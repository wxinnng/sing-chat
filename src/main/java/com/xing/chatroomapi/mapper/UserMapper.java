package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author:WangXing
 * @DATE:2024/4/26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
