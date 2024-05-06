package com.xing.chatroomapi.websocket;

import com.alibaba.fastjson.JSON;
import com.xing.chatroomapi.pojo.entity.Message;
import lombok.extern.slf4j.Slf4j;
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


    /**
     * 连接建立成功调用的方法,登录的时候调用这个方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userid") Integer userid) {
        log.info("{} 与服务器进行连接.",userid);
        sessionMap.put(userid , session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("userid") String userid) {
        log.info("用户：{}，发送信息：{}",userid,message);

        for (Session value : sessionMap.values()) {
            System.out.println(value);
        }
        Message parseObject = null;
        try{
            parseObject = JSON.parseObject(message, Message.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        parseObject.setCreateTime(LocalDateTime.now());  //设置发送时间

        //拿到target的session，如果是群的话，需要群发
        if(parseObject.getType() == 1){
            //单个发

            //获取session
            Session targetSession = sessionMap.get(parseObject.getTargetId());

            if(targetSession != null){
                //说明用户在线，直接把信息发送给他
                try {
                    targetSession.getBasicRemote().sendText(parseObject.getBody());
                } catch (IOException e) {
                    log.error("消息发送失败 : {}",e.getMessage());
                }
            }
            //把信息放到MongoDB中去



        }else{
            //群发


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
    }


}
