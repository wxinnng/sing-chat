package com.xing.chatroomapi.util;

import java.util.Random;

/**
 * @Author:WangXing
 * @DATE:2024/5/6
 */
public class StringUtil {

    private final static String tmp = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
    public static String getIdByUserIds(Integer user1,Integer user2){
        return user1 < user2 ? user1+""+user2: user2 + "" +user1;
    }

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(tmp.length());
            sb.append(tmp.charAt(randomIndex));
        }
        return sb.toString();
    }

}
