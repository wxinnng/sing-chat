package com.xing.chatroomapi.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author:WangXing
 * @DATE:2024/4/30
 */

@Data
@TableName("application")
public class Application {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer fromUser;
    private Integer toUser;
    private Integer type;
    private Integer status;
    private Integer groupId;
    private LocalDateTime createTime;
}
