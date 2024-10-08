package com.xing.chatroomapi.websocket;

import com.alibaba.fastjson.JSON;
import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.pojo.entity.Message;
import com.xing.chatroomapi.pojo.vo.ApplicationResultVO;
import com.xing.chatroomapi.service.ChatService;
import com.xing.chatroomapi.service.GroupService;
import com.xing.chatroomapi.util.RedisUtil;
import com.xing.chatroomapi.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:WangXing
 * @DATE:2024/5/2
 */
@Component
@Slf4j
@ServerEndpoint("/chat/{userid}")
public class ChatServer {

    //保存在线的会话
    private static Map<Integer, Session> sessionMap = new HashMap<>();

    private static ChatService chatService;

    private static RedisUtil redisUtil;

    private static GroupService groupService;

    @Autowired
    public void setApplicationContext(ChatService chatService){
        ChatServer.chatService = chatService;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil){
        ChatServer.redisUtil = redisUtil;
    }

    @Autowired
    private void setGroupService(GroupService groupService){ChatServer.groupService = groupService;}


    /**
     * 连接建立成功调用的方法,登录的时候调用这个方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userid") Integer userid) {
        log.info("{} 与服务器进行连接.", userid);
        sessionMap.put(userid, session);
        //把用户上线的信息放到redis中去
        redisUtil.set(MessageConstant.USER_ONLINE_REDIS_KEY + userid, "1");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("userid") String userid) {
        log.info("用户：{}，发送信息：{}",userid,message);

        Message parseObject = null;
        try{
            parseObject = JSON.parseObject(message, Message.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        //设置发送消息的时间
        parseObject.setCreateTime(LocalDateTime.now());  //设置发送时间

        //设置chatId
        parseObject.setChatId(StringUtil.getIdByUserIds(parseObject.getUserId(), parseObject.getTargetId()));

        //拿到target的session，如果是群的话，需要群发
        if(parseObject.getType() == 1){
            //单个发

            //获取session
            Session targetSession = sessionMap.get(parseObject.getTargetId());

            if(targetSession != null){
                //说明用户在线，直接把信息发送给他
                try {
                    targetSession.getBasicRemote().sendText(JSON.toJSONString(parseObject));
                } catch (IOException e) {
                    log.error("消息发送失败 : {}",e.getMessage());
                }
            }
            //把信息放到MongoDB中去
            chatService.saveChatMessage(parseObject);

        }else{
            //群发(前提是在线)
            //1.先找到对应的group下所有的用户
            List<Integer> ids = groupService.getMemberIdsByGroupId(parseObject.getTargetId());

            //1.2去掉自己
            ids.remove(parseObject.getUserId());

            //2.遍历list，给在线的用户推送信息
            for (Integer id : ids) {
                if(sessionMap.containsKey(id)){
                    try {
                        sessionMap.get(id).getBasicRemote().sendText(JSON.toJSONString(parseObject));
                    } catch (IOException e) {
                        log.info("群发信息失败！");
                    }
                }
            }

            //把信息放到mongoDB中,群聊信息中，groupId 就是 chatId
            parseObject.setChatId(parseObject.getTargetId()+"");

            chatService.saveChatMessage(parseObject);
        }

    }


    /**
     * @description: 给前端发送信息
     * @param: []
     * @return: void
     */
    public void sendMessageToClient(ApplicationResultVO resultVO){

        //拿到目标用户的session
        Session session = sessionMap.get(resultVO.getUserId());


        //对应的用户没在线，也没有必要推送信息了
        if(session == null)
            return;

        try {
            //发送信息
            session.getBasicRemote().sendText(JSON.toJSONString(resultVO));

        } catch (IOException e) {
            System.err.println("信息推送失败!");
        }
    }


    /**
     * 连接关闭调用的方法(用户下线)
     * @param userid
     */
    @OnClose
    public void onClose(@PathParam("userid") Integer userid) {
        log.info("{} :关闭连接" , userid);
        sessionMap.remove(userid);
        //关闭连接，在redis中删除对应用户的连接信息
        redisUtil.del(MessageConstant.USER_ONLINE_REDIS_KEY+userid);
    }
}
