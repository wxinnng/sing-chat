package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.dto.CreateGroupUserDTO;
import com.xing.chatroomapi.pojo.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/4/30
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    /**
     * @description: 批量插入member信息
     * @param: [java.util.List<com.xing.chatroomapi.pojo.entity.Member>]
     * @return: void
     */
    void saveBatch(List<Member> members);

    List<CreateGroupUserDTO> getMemberDTOByGroupId(Integer id);
}
