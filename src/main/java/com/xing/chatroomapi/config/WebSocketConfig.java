package com.xing.chatroomapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author:WangXing
 * @DATE:2024/5/2
 */
@Configuration
public class WebSocketConfig {
    @Bean
    //注入ServerEndpointExporter，自动注册使用@ServerEndpoint注解的
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
