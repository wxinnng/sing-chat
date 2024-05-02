package com.xing.chatroomapi.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xing.chatroomapi.pojo.ChatSession;
import com.xing.chatroomapi.pojo.Relation;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author:WangXing
 * @DATE:2024/4/30
 */
@Data
@TableName("group_info")
public class Group implements Serializable,ChatSession,Relation{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String groupName;
    private Integer ownerId;
    private String avatar;
    private Integer curNum;
    private String introduce;
    private LocalDateTime createTime;
}
