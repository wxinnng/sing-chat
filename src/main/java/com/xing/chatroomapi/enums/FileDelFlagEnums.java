package com.xing.chatroomapi.enums;

/**
 * @Author:86198
 * @DATE:2024/3/14 16:16
 * @DESCRIPTION:
 * @VERSION:1.0
 */
public enum FileDelFlagEnums {
    DEL(0,"删除"),
    RECYCLE(1,"回收站"),
    USING(2,"使用中");

    private Integer flag;
    private String desc;
    FileDelFlagEnums(Integer flag,String desc){
        this.flag = flag;
        this.desc = desc;
    }
    public Integer getFlag() {
        return this.flag;
    }
    public String getDesc() {
        return this.desc;
    }
}
