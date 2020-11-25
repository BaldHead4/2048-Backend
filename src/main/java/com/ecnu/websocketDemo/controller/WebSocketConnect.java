package com.ecnu.websocketDemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.websocketDemo.Utils.WebSocketUtil;
import com.ecnu.websocketDemo.entity.PlayRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import sun.misc.ThreadGroupUtils;

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
 *
 * @ServerEndpoint 这个注解有什么作用？
 *
 * 这个注解用于标识作用在类上，它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 *
 */

@Slf4j
@Component
@ServerEndpoint("/websocket/{id}")
public class WebSocketConnect {

    /*与某个客户端的连接对话，需要通过它来给客户端发送消息*/
    private Session session;

    /* 标识当前连接客户端的用户名*/
    private String id;

    /*如果分配了房间则标定房间*/
    private PlayRoom playRoom;

    public Session getSession() {
        return session;
    }

    public String getId() {
        return id;
    }

    public void setPlayRoom(PlayRoom playRoom) {
        this.playRoom = playRoom;
    }

    /*
    * 玩家进入房间时，该方法响应，并将玩家 id 与 Session 加入 HashMap 中
    * */
    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "id") String id){

        this.session = session;
        this.id = id;
        // id是用来表示唯一客户端，如果需要指定发送，需要指定发送通过id来区分
        WebSocketUtil.webSocketSet.put(id,this);

        log.info("[WebSocket] 连接成功，当前连接人数为：={}",WebSocketUtil.webSocketSet.size());
        log.info("当前在线成员有：" + WebSocketUtil.webSocketSet.toString());
    }


    /*
    * 客户端关闭后，将该 id 从集合中移除
    * 但不从难度队列中移除，难度队列用于判断是否正在游戏中，需要进行重连
    * （不过在难度队列中并不代表正在游戏，是否需要维护一个游戏队列）
    * */
    @OnClose
    public void OnClose(){
        WebSocketUtil.webSocketSet.remove(this.id);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", WebSocketUtil.webSocketSet.size());
    }

    /*
    * 通过接受的消息的类型判断需要调用哪一个方法
    * 0： 前端点击匹配按钮，提供用户名尝试进行匹配
    * 1： 游戏过程中进行游戏状态的传递
    * 2：
    * */
    @OnMessage
    public void OnMessage(String message){
        System.out.println("message:" + message);

        JSONObject obj = JSON.parseObject(message);
        Integer method = (Integer) obj.get("method");

        List<String> list = null;

        if (WebSocketUtil.easyList.contains(id)){
            list = WebSocketUtil.easyList;
        } else if (WebSocketUtil.hardList.contains(id)) {
            list = WebSocketUtil.hardList;
        }

        switch (method){
            case 0:
                WebSocketUtil.openConnection(JSON.toJSONString(obj));
                break;
            case 1:
                obj.put("sender", "system");
                WebSocketUtil.PlayRoomGroupSending((JSON.toJSONString(obj)), playRoom);
                break;
            case 2:
                /*掉线了*/
                //WebSocketUtil.PlayRoomGroupSending(JSON.toJSONString(obj), playRoom);
                break;
            default:
                ;
        }

        log.info("[WebSocket] 收到消息：{}",message);
    }


}