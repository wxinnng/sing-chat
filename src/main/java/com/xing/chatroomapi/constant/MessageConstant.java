package com.xing.chatroomapi.constant;

/**
 *  @Author: WangXing
 *  @Date: 2024/4/26
 *  @Descr IPTION:  常量
 */
public interface MessageConstant {
    String SECRETE_KEY = "SingingChat";
    Long  EXPIRATION_TIME = 3600000000L;
    Integer LOGIN_ERROR = 1001;
    Integer SERVER_ERROR = 500;
    Integer REGISTER_ERROR = 1002;
}
