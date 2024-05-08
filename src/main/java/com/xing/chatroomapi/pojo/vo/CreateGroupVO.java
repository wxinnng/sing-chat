package com.xing.chatroomapi.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/5/7
 */

@Data
public class CreateGroupVO {
    private String groupName;
    private String groupInfo;
    private List<Integer> memberIds;
}
