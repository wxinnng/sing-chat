package com.xing.chatroomapi.pojo.vo;

import lombok.Data;

/**
 * @Author:WangXing
 * @DATE:2024/4/28
 */

@Data
public class UserVO {
    private Integer id;
    private String nickName;
    private String avatar;
    private String password;
    private String token;
}
