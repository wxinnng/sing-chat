package com.xing.chatroomapi.util;

import com.xing.chatroomapi.pojo.entity.User;

/**
 * @Author:WangXing
 * @DATE:2024/4/28
 */
public class BaseContext {

    public static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(User user) {
        threadLocal.set(user);
    }

    public static User getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
