package com.ecnu.websocketDemo.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.websocketDemo.controller.WebSocketConnect;
import com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtil {

    /**
     *  用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    public static ConcurrentHashMap<String, WebSocketConnect> webSocketSet = new ConcurrentHashMap<>();

    public static List<String> userIdForEasy = new ArrayList<>();

    public static List<String> userIdForHard = new ArrayList<>();

    /*最大对局人数*/
    public static final Integer MAX_PLAYER = 4 ;

    /**
     * 需要群发的数据有
     * 1. 对局进行时的游戏信息
     * 2.
     *
     *
     *
     * 群发
     * @param message
     */
    public static void GroupSending(String message){

        JSONObject jsonObject = JSON.parseObject(message);
        jsonObject.put("status", "success");

        for (String id : webSocketSet.keySet()){
            try {
                webSocketSet.get(id).getSession().getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 难度队列群发
     * 将消息发送给相应难度队列的成员
     * @param message
     */
    public static void ListGroupSending(String message, List<String> list){

        JSONObject jsonObject = JSON.parseObject(message);
        jsonObject.put("status", "success");

        for (String id : list){
            try {
                webSocketSet.get(id).getSession().getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 1. 在难度队列已满的情况下，发送点对点消息，通知玩家该难度队列已满，无法加入，请重新尝试连接或改变难度
     *
     * 指定发送
     * @param id
     * @param message
     */
    public static void AppointSending(String id, String message){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("message", message);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
