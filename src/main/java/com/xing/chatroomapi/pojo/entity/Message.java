package com.xing.chatroomapi.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author:WangXing
 * @DATE:2024/5/2
 */
@Data
public class Message implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer targetId;
    private Integer type;
    private String body;
    private String messageType;
    private LocalDateTime createTime;

}
