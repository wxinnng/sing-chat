package com.xing.chatroomapi.service.impl;

import com.xing.chatroomapi.pojo.entity.Message;
import com.xing.chatroomapi.service.ChatService;
import com.xing.chatroomapi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/5/6
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveChatMessage(Message message) {
        mongoTemplate.insert(message);
    }

    @Override
    public List<Message> getMessageByChatId(Integer user1, Integer user2, Integer type) {

        //返回的

        Query query = new Query();
        if(type == 1){
            //好友聊天记录
            query.addCriteria(Criteria.where("chatId").is(StringUtil.getIdByUserIds(user1,user2)));
        }else{
            //群聊天记录:user2 是groupId,也就是chatID。
            query.addCriteria(Criteria.where("targetId").is(user2));
        }
        return mongoTemplate.find(query, Message.class, "chat_message");

    }
}
