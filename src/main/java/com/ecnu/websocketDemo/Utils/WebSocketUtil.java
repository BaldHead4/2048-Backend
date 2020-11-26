package com.ecnu.websocketDemo.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.websocketDemo.controller.WebSocketConnect;
import com.ecnu.websocketDemo.entity.PlayRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public static final Integer MAX_PLAYER = 2 ;

    /**
     * 难度队列群发
     * 将消息发送给相应房间的成员
     * @param message
     */
    public static void playRoomGroupSending(String message, PlayRoom playRoom){
        for (String id : playRoom.getPlayers()){
            try {
                if (webSocketSet.get(id)!=null) {
                    webSocketSet.get(id).getSession().getBasicRemote().sendText(message);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 1. 在难度队列已满的情况下，发送点对点消息，通知玩家房间已满，无法加入，请重新尝试连接或改变难度
     *
     * 指定发送
     * @param id
     * @param message
     */
    public static void AppointSending(String id, String message){
        try {
            webSocketSet.get(id).getSession().getBasicRemote().sendText(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * 前端 onOpen 之后，通过该接口传递 json 数据 id, username, difficulty
     * 1. 如果某个游戏房间中包含该 id ，则说明玩家正在游戏中，需要重连
     * 2. 如果游戏房间中不包含，但等待队列中包含，这种情况不需要处理
     * 3. 新玩家则加入等待队列
     * */
    public static void openConnection(String message) {
        JSONObject jsonObject = JSON.parseObject(message);

        Integer difficulty = (Integer) jsonObject.get("difficulty");
        String id = (String) jsonObject.get("id");
        String username = (String) jsonObject.get("username");
        WebSocketConnect connect = webSocketSet.get(id);

        if (webSocketSet.get(id) != null) {
            boolean isPlayer = false;
            PlayRoom playRoomToConnect = null;
            for (PlayRoom playRoom : playRoomList) {
                if (playRoom.getPlayers().contains(id)) {
                    isPlayer = true;
                    playRoomToConnect = playRoom;
                    break;
                }
            }
            if (isPlayer){
                //如果游戏房间中包含该 id ，则说明玩家正在游戏中，需要重连
                reconnect(id, playRoomToConnect);
            } else if (easyList.contains(id) || hardList.contains(id)) {
                //如果游戏房间中不包含，但等待队列中包含，这种情况不需要处理

            } else {
                //还未加入等待队列则加入等待队列，
                if (difficulty == 1 ) {
                    if (easyList.size() >= MAX_PLAYER) {
                        AppointSending(id, JSONUtil.buildTextJSONObject( "房间已满，请重新匹配或重新选择难度").toJSONString());
                    } else {
                        easyList.add(id);
                        if (easyList.size() == MAX_PLAYER) {
                            PlayRoom playRoom = new PlayRoom(new ArrayList<>(easyList), difficulty);
                            playRoomList.add(playRoom);
                            for (String player : easyList) {
                                if(webSocketSet.containsKey(player)) {
                                    webSocketSet.get(player).setPlayRoom(playRoom);
                                }
                            }
                            playRoomGroupSending(JSONUtil.buildTextJSONObject( "加入了简单房间 " + playRoom.getPrId()).toJSONString() ,playRoom);
                            easyList.clear();
                        }
                    }
                }else if (difficulty == 2){
                    if (hardList.size() >= MAX_PLAYER) {
                        AppointSending(id, JSONUtil.buildTextJSONObject( "房间已满，请重新匹配或重新选择难度").toJSONString());
                    } else {
                        hardList.add(id);
                        if (hardList.size() == MAX_PLAYER) {
                            PlayRoom playRoom = new PlayRoom(new ArrayList<>(hardList), difficulty);
                            playRoomList.add(playRoom);
                            for (String player : hardList) {
                                if(webSocketSet.containsKey(player)) {
                                    webSocketSet.get(player).setPlayRoom(playRoom);
                                }
                            }
                            playRoomGroupSending(JSONUtil.buildTextJSONObject("加入了困难房间 " + playRoom.getPrId()).toJSONString() ,playRoom);
                            hardList.clear();
                        }
                    }
                }
            }
        }

    }

    /*该重连方法将游戏房间对局信息发送给该 player
    * 保存的对局信息的格式为 JSONString 字符串
    * */
    private static void reconnect(String id, PlayRoom playRoom) {
        String msg = JSONUtil.buildTextJSONObject("玩家 "+ id +" 正在重连").toJSONString();
        WebSocketUtil.playRoomGroupSending(msg, playRoom);

        WebSocketUtil.AppointSending(id, JSONUtil.buildReConnectJSONObject(playRoom).toJSONString());
        webSocketSet.get(id).setPlayRoom(playRoom);
    }
}
