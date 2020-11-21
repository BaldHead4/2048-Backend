package com.ecnu.websocketDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: liaoshiyao
 * @Date: 2019/1/11 11:48
 * @Description: websocket 服务类
 */

/**
 *
 * @ServerEndpoint 这个注解有什么作用？
 *
 * 这个注解用于标识作用在类上，它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 *
 */

@Slf4j
@Component
@ServerEndpoint("/websocket/{userID}")
public class WebSocketConnect {

    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的用户名
     */
    private String userID;

    /**
     *  用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<String, WebSocketConnect> webSocketSet = new ConcurrentHashMap<>();

    private static List<String> userIdForEasy = new ArrayList<>();

    private static List<String> userIdForHard = new ArrayList<>();

    /*
    * 玩家进入房间时，该方法响应，并将玩家 UseruserID 与 Session 加入 HashMap 中
    * */
    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "userID") String userID){
        this.session = session;
        this.userID = userID;

        // userID是用来表示唯一客户端，如果需要指定发送，需要指定发送通过userID来区分
        webSocketSet.put(userID,this);


        log.info("[WebSocket] 连接成功，当前连接人数为：={}",webSocketSet.size());
        log.info("当前在线成员有：" + webSocketSet.toString());
    }


    @OnClose
    public void OnClose(){
        webSocketSet.remove(this.userID);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}",webSocketSet.size());
    }


    /*
    * 在 2048 游戏中，玩家每一次进行操作都会向服务端发送信息，服务端接收之后将信息广播给其他成员
    * 操作的类型可以是
    * 游戏流程： 准备 开始 游戏结束
    * 一个问题是：直接将计算好的矩阵做消息发送，还是发送操作让接收者进行匹配
    * 游戏过程： 上下左右 移动
    * */
    @OnMessage
    public void OnMessage(String message){

        log.info("[WebSocket] 收到消息：{}",message);

        GroupSending(message);

    }

    /**
     * 群发
     * @param message
     */
    public void GroupSending(String message){
        for (String userID : webSocketSet.keySet()){
            try {
                webSocketSet.get(userID).session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定发送
     * @param userID
     * @param message
     */
    public void AppointSending(String userID,String message){
        try {
            webSocketSet.get(userID).session.getBasicRemote().sendText(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}