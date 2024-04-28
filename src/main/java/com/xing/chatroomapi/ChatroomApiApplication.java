package com.xing.chatroomapi;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan(basePackages = "com.xing.chatroomapi.mapper")
public class ChatroomApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatroomApiApplication.class, args);
    }

}
