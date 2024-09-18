package com.xing.chatroomapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Author:WangXing
 * @DATE:2024/8/7
 */
@Configuration
public class AsyncConfig {
    @Bean(name = "myAsyncThreadPool")
    public ThreadPoolTaskExecutor asyncThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程数
        executor.setMaxPoolSize(10); // 最大线程数
        executor.setQueueCapacity(200); // 队列大小
        executor.setThreadNamePrefix("Async-"); // 线程前缀名
        executor.initialize();
        return executor;
    }
}

