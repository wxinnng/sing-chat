package com.xing.chatroomapi.util;

/**
 * @Author:WangXing
 * @DATE:2024/5/6
 */
public class StringUtil {
    public static String getIdByUserIds(Integer user1,Integer user2){
        return user1 < user2 ? user1+""+user2: user2 + "" +user1;
    }
}
