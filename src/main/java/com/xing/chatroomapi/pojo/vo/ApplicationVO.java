package com.xing.chatroomapi.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author:WangXing
 * @DATE:2024/4/30
 */
@Data
public class ApplicationVO {
    private Integer id;
    private Integer fromUser;
    private String nickName;
    private String avatar;
    private Integer toUser;
    private Integer type;
    private Integer status;
    private LocalDateTime createTime;
}
