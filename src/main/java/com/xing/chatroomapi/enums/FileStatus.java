package com.xing.chatroomapi.enums;

/**
 * @Author:86198
 * @DATE:2024/3/16 15:47
 * @DESCRIPTION:
 * @VERSION:1.0
 */
public enum FileStatus {
    TRANSFER(0,"转码中"),
    TRANSFER_FAIL(1,"转码失败"),
    USING(2,"使用中");
    private Integer status;
    private String desc;
    FileStatus(Integer status,String desc){
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
