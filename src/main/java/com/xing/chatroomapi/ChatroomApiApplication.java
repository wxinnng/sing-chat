package com.xing.chatroomapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@MapperScan(basePackages = "com.xing.chatroomapi.mapper")
@CrossOrigin
public class ChatroomApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatroomApiApplication.class, args);
    }


}
