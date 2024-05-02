package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.entity.Member;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author:WangXing
 * @DATE:2024/4/30
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

}
