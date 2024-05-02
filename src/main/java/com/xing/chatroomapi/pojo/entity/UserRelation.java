package com.xing.chatroomapi.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xing.chatroomapi.pojo.Relation;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author:WangXing
 * @DATE:2024/5/1
 */
@Data
@TableName("user_relation")
public class UserRelation implements Serializable,Relation  {
    private Integer id;
    private Integer UserOne;
    private Integer UserTwo;
    private LocalDateTime createTime;
}
