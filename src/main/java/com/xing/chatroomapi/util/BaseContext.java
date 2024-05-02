package com.xing.chatroomapi.util;

import com.xing.chatroomapi.pojo.entity.User;

/**
 * @Author:WangXing
 * @DATE:2024/4/28
 */
public class BaseContext {

    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(Integer id) {
        threadLocal.set(id);
    }

    public static Integer getCurrentUser() {
        return threadLocal.get();
    }

    public static void removeCurrentUser() {
        threadLocal.remove();
    }

}
