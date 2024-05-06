package com.xing.chatroomapi.service;

import com.xing.chatroomapi.pojo.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/5/6
 */
public interface ChatService  {
    /**
     * @description: 保存聊天记录
     * @param: [com.xing.chatroomapi.pojo.entity.Message]
     * @return: void
     */
    void saveChatMessage(Message message);


    /**
     * @description: 通过chatId获得聊天记录
     * @param: [java.lang.String]
     * @return: java.util.List<com.xing.chatroomapi.pojo.entity.Message>
     */
    List<Message> getMessageByChatId(Integer user1,Integer user2,Integer type);

}
