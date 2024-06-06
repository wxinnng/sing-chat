package com.xing.chatroomapi.enums;

/**
 * 异常枚举类
 */
public enum ExceptionEnums {


    FILE_NAME_NOT_NULL("0001", "文件名不能为空"),
    BUCKET_NAME_NOT_NULL("0002", "桶名称不能为空"),
    FILE_NOT_EXIST("0003", "文件不存在"),
    BUCKET_NOT_EXIST("0004", "桶不存在"),
    BUCKET_NAME_NOT_EXIST("0005", "桶不存在，需要先创建桶在创建文件夹");//枚举类如果写方法的话，此处需要写分号

    private String code;

    private String msg;

    ExceptionEnums(String ecode, String emsg) {
        this.code = ecode;
        this.msg = emsg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ExceptionEnums statOf(String code) {
        for (ExceptionEnums state : values())
            if (state.getCode().equals(code))
                return state;
        return null;
    }
}
