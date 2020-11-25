package com.ecnu.websocketDemo.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.websocketDemo.controller.WebSocketConnect;
import com.ecnu.websocketDemo.entity.PlayRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtil {

    /**
     *  用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    public static ConcurrentHashMap<String, WebSocketConnect> webSocketSet = new ConcurrentHashMap<>();

    public static List<String> easyList = new ArrayList<>();

    public static List<String> hardList = new ArrayList<>();
    
    public static List<PlayRoom> playRoomList = new ArrayList<>();

    /*最大对局人数*/
    public static final Integer MAX_PLAYER = 4 ;

    /**
     * 群发
     * @param message
     */
    /*public static void GroupSending(String message){

        JSONObject jsonObject = JSON.parseObject(message);
        jsonObject.put("sender", "success");
        message = JSON.toJSONString(jsonObject);

        for (String id : webSocketSet.keySet()){
            try {
                webSocketSet.get(id).getSession().getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/

    /**
     * 难度队列群发
     * 将消息发送给相应难度队列的成员
     * @param message
     */
    /*public static void ListGroupSending(String message, List<String> list){

        JSONObject jsonObject = JSON.parseObject(message);
        jsonObject.put("sender", "success");
        message = JSON.toJSONString(jsonObject);

        for (String id : list){
            try {
                webSocketSet.get(id).getSession().getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/

    /**
     * 难度队列群发
     * 将消息发送给相应房间的成员
     * @param message
     */
    public static void PlayRoomGroupSending(String message, PlayRoom playRoom){

        JSONObject jsonObject = JSON.parseObject(message);
        //jsonObject.put("sender", "success");
        message = JSON.toJSONString(jsonObject);

        for (String id : playRoom.getPlayers()){
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
            jsonObject.put("sender", "system");
            message = JSON.toJSONString(jsonObject);

            webSocketSet.get(id).getSession().getBasicRemote().sendText(message);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * 前端 onOpen 之后，通过该接口传递 json 数据 id, username, difficulty
     * 判断是否已经在难度队列中，
     * 1. 如果不在难度队列中则添加到相应的难度队列中
     * 如果在难度队列中则说明已经开始游戏，需要重连
     * */
    public static JSONObject openConnection(String message) {
        JSONObject jsonObject = JSON.parseObject(message);

        //System.out.println("onConnection: success");
        //System.out.println(jsonObject.toString());

        Integer difficulty = (Integer) jsonObject.get("difficulty");
        String id = (String) jsonObject.get("id");
        String username = (String) jsonObject.get("username");
        WebSocketConnect connect = webSocketSet.get(id);

        //System.out.println(id + " " + username + " " + difficulty);
        //System.out.println("onConnect: " + webSocketSet);
        //System.out.println(webSocketSet.get(id));

        if (webSocketSet.get(id) != null) {
            boolean isPlayer = false;
            for (PlayRoom playRoom : playRoomList) {
                if (playRoom.getPlayers().contains(id)) {
                    isPlayer = true;
                    break;
                }
            }
            if (isPlayer){
                //如果游戏房间中包含该 id ，则说明玩家正在游戏中，需要重连
                // reconnect();
            } else if (easyList.contains(id) || hardList.contains(id)) {
                //如果游戏房间中不包含，但等待队列中包含，这种情况不需要处理

            } else {
                //还未加入等待队列则加入等待队列，
                if (difficulty == 1 ) {
                    if (easyList.size() >= MAX_PLAYER) {
                        AppointSending(id, "简单难度玩家已满，请重新匹配或重新选择难度");
                    } else {
                        easyList.add(id);
                        if (easyList.size() == MAX_PLAYER) {
                            PlayRoom playRoom = new PlayRoom(new ArrayList<>(easyList), difficulty);
                            playRoomList.add(playRoom);
                            webSocketSet.get(id).setPlayRoom(playRoom);
                            PlayRoomGroupSending(buildTextJSONObject(id, "加入了困难房间 " + playRoom.getPrId()).toJSONString() ,playRoom);
                            easyList.clear();
                        }
                    }
                }else if (difficulty == 2){
                    if (hardList.size() >= MAX_PLAYER) {
                        AppointSending(id, "困难难度玩家已满，请重新匹配或重新选择难度");
                    } else {
                        hardList.add(id);
                        if (hardList.size() == MAX_PLAYER) {
                            PlayRoom playRoom = new PlayRoom(new ArrayList<>(hardList), difficulty);
                            playRoomList.add(playRoom);
                            webSocketSet.get(id).setPlayRoom(playRoom);
                            PlayRoomGroupSending(buildTextJSONObject(id, "加入了困难房间 " + playRoom.getPrId()).toJSONString() ,playRoom);
                            hardList.clear();
                        }
                    }
                }
            }
        }

        return null;
    }

    public static JSONObject buildTextJSONObject(String id, String message) {
        JSONObject textMsg = new JSONObject();
        //封装文字信息的 JSON 对象
        textMsg.put("message", message);
        textMsg.put("id", id);
        textMsg.put("sender", "system");
        return textMsg;
    }
}
