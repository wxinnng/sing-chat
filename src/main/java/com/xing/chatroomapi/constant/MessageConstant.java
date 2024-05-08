package com.xing.chatroomapi.constant;

/**
 *  @Author: WangXing
 *  @Date: 2024/4/26
 *  @Descr IPTION:  常量
 */
public interface MessageConstant {
    Integer PENDING_APPROVAL = 0;
    Integer APPROVED = 1;
    Integer REJECTED = 2;
    String SECRETE_KEY = "SingingChat";
    Long  EXPIRATION_TIME = 3600000000L;
    Integer SERVER_ERROR = 500;
    Integer LOGIN_ERROR = 1001;
    Integer REGISTER_ERROR = 1002;
    Integer TARGET_NOT_FOUND = 1003;
    Integer ALREADY_RELATION = 1004;
    Integer ALREADY_POST_REQUEST = 1005;
    Integer CANNOT_BE_YOURSELF_FRIEND = 1005;
    Integer PARAMS_ERROR = 1004;
    Integer USER_INFO_ERROR = 1005;
    String TOKEN_HEADER = "token";
    String COLLECTION_NAME = "chat_message";
    String USER_ONLINE_REDIS_KEY = "user_online:";
}
