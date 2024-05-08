package com.xing.chatroomapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xing.chatroomapi.mapper.GroupMapper;
import com.xing.chatroomapi.mapper.MemberMapper;
import com.xing.chatroomapi.pojo.entity.Group;
import com.xing.chatroomapi.pojo.entity.Member;
import com.xing.chatroomapi.service.GroupService;
import com.xing.chatroomapi.util.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *@Author:WangXing
 *@DATE:2024/5/7
*/
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(String groupName, String groupInfo, List<Integer> memberIds) {

        //整个群创建的时间
        LocalDateTime now = LocalDateTime.now();

        Group group = new Group();
        group.setCreateTime(now);
        group.setIntroduce(groupInfo);
        group.setGroupName(groupName);
        group.setOwnerId(BaseContext.getCurrentUser());
        group.setCurNum(1 + memberIds.size());

        groupMapper.insert(group); //插入数据

        //将成员信息插入到member表中
        List<Member> members = new ArrayList<>();

        //群主信息
        Member owner = new Member();
        owner.setIsOwner(1);
        owner.setGroupId(group.getId());
        owner.setJoinTime(now);
        System.err.println(BaseContext.getCurrentUser());
        owner.setUserId(BaseContext.getCurrentUser());
        members.add(owner);

        //成员信息
        for (Integer memberId : memberIds) {
            Member member = new Member();
            member.setUserId(memberId);
            member.setIsOwner(0);
            member.setJoinTime(now);
            member.setGroupId(group.getId());
            members.add(member);
        }

        System.err.println(members);

        //批量插入用户信息
        memberMapper.saveBatch(members);

    }

    @Override
    public Group getGroupRelationDetail(Integer id) {
        Group result = new Group();

        //查询的最后结果
        result = groupMapper.selectById(id);

        //填充memberList
        result.setMemberList(memberMapper.getMemberDTOByGroupId(id));

        return result;
    }
}
