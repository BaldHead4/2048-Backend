package com.ecnu.websocketDemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.websocketDemo.Utils.JSONUtil;
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

    public PlayRoom getPlayRoom() {
        return playRoom;
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
    * 如果玩家掉线，则向房间里的其他玩家广播该玩家掉线信息
    * */
    @OnClose
    public void OnClose(){
        WebSocketUtil.webSocketSet.remove(this.id);
        /*掉线了*/
        if (this.playRoom != null) {
            String msg = JSONUtil.buildTextJSONObject("玩家 " + this.id + " 掉线了").toJSONString();
            WebSocketUtil.playRoomGroupSending(msg, this.playRoom);
        }
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

        JSONObject obj = JSON.parseObject(message);
        Integer method = (Integer) obj.get("method");

        switch (method){
            case 0:
                WebSocketUtil.openConnection(JSON.toJSONString(obj));
                break;
            case 1:
                if (this.playRoom != null) {
                    /*将该 player 对局信息存储到相应 playroom 中*/
                    String id = (String) obj.get("id");
                    this.playRoom.getGameMessage().put(id, message);
                    /*广播该 player 对局信息*/
                    obj.put("sender", "player");
                    WebSocketUtil.playRoomGroupSending((JSON.toJSONString(obj)), this.playRoom);
                }
                break;
            case 2:

                break;
            default:
                ;
        }

        log.info("[WebSocket] 收到消息：{}",message);
    }


}