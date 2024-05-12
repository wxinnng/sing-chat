package com.xing.chatroomapi.pojo.vo;

import lombok.Data;

/**
 * @Author:WangXing
 * @DATE:2024/5/8
 */
@Data
public class ApplicationResultVO {
    private String msg;
    private Integer userId;
    private Integer pushType;
    private Integer result;  //1 同意，  2 失败
}
