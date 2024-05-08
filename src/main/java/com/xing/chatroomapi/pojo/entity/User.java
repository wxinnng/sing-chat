package com.xing.chatroomapi.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xing.chatroomapi.pojo.ChatSession;
import com.xing.chatroomapi.pojo.Relation;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author:WangXing
 * @DATE:2024/4/26
 */
@Data
@TableName(value="user")
public class User implements ChatSession, Relation {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String nickName;
    private String avatar;
    private String password;
}
