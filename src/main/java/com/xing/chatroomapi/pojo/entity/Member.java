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
@TableName(value = "member")
public class Member {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer groupId;
    private Integer isOwner;
    private LocalDateTime joinTime;
}
