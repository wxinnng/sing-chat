package com.xing.chatroomapi;

import com.xing.chatroomapi.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatroomApiApplicationTests {

    @Autowired
    private UserMapper userMapper;


    @Test
    void contextLoads() {

    }



}
