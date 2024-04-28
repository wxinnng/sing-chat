package com.xing.chatroomapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xing.chatroomapi.mapper.UserMapper;
import com.xing.chatroomapi.pojo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ChatroomApiApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("id",1001);
        System.out.println(userMapper.selectOne(query));
    }

}
