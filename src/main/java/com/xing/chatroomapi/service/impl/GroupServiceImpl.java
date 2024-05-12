package com.xing.chatroomapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xing.chatroomapi.exception.BusinessException;
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

        //批量插入用户信息
        memberMapper.saveBatch(members);

    }

    @Override
    public List<Integer> getMemberIdsByGroupId(Integer targetId) {
        return memberMapper.getMemberIdsByGroupId(targetId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outGroup(Integer id) throws BusinessException {
        //判断是不是群主
        LambdaUpdateWrapper<Member> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(Member::getGroupId,id).eq(Member::getUserId,BaseContext.getCurrentUser());
        Member member = memberMapper.selectOne(queryWrapper);

        if(member == null){
            throw new BusinessException("参数异常！");
        }

        if(member.getIsOwner() == 1){
            //群主，解散群聊
            //删除member信息
            queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(Member::getGroupId,id);
            memberMapper.delete(queryWrapper);

            //删除group信息
            groupMapper.deleteById(id);

        }else{
            //群友，退出群聊
            memberMapper.delete(queryWrapper);
            groupMapper.deCurNum(id);
        }

    }

    @Override
    public Group getGroupRelationDetail(Integer id) {
        Group result = new Group();

        //查询的最后结果
        result = groupMapper.getGroupRelationDetail(id,BaseContext.getCurrentUser());

        //填充memberList
        result.setMemberList(memberMapper.getMemberDTOByGroupId(id));

        return result;
    }
}
