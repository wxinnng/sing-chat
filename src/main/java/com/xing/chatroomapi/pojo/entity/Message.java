package com.xing.chatroomapi.pojo.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.time.LocalDateTime;

/**
 * @Author:WangXing
 * @DATE:2024/5/2
 */
@Data
@Document("chat_message")
public class Message implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private String id;
    private String chatId;
    private Integer userId;
    private Integer targetId;
    private Integer type;
    private String body;
    private Integer messageType;
    private LocalDateTime createTime;
}
