package com.ecnu.websocketDemo.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.websocketDemo.entity.PlayRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONUtil {

    //封装游戏开始时发送房间里其他玩家的用户名和 id 信息的 JSON 对象
    public static JSONObject buildOtherPlayerMsgJSONObject(PlayRoom playRoom) {
        JSONObject textMsg = new JSONObject();
        List<String> players = playRoom.getPlayers();
        List<JSONObject> jsonObjects = new ArrayList<>();
        textMsg.put("playerList", players);
        for (String player : players) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", player);
            jsonObject.put("username", playRoom.getIdToName().get(player));
            jsonObjects.add(jsonObject);
        }
        textMsg.put("playerList", jsonObjects);
        textMsg.put("type", 0);
        System.out.println(textMsg.toJSONString());
        return textMsg;
    }

    /*封装时间到的 JSON 对象*/
    public static JSONObject buildTimeOutJSONObject(String message) {
        JSONObject textMsg = new JSONObject();
        textMsg.put("message", message);
        textMsg.put("type", 2);
        return textMsg;
    }

    /*封装重连时的发送的游戏对局状态 JSON 对象
    * 返回该局的玩家以及其游戏状态
    * */
    public static JSONObject buildReConnectJSONObject(PlayRoom playRoom) {
        Map<String, String> gameMessage = playRoom.getGameMessage();
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> playerList = new ArrayList<>();
        for (String playerId : playRoom.getPlayers()) {
            JSONObject object = new JSONObject();
            object.put("id", playerId);
            object.put("username", playRoom.getIdToName().get(playerId));
            object.put("gameMsg", gameMessage.get(playerId));
            playerList.add(object);
        }
        jsonObject.put("playerList", playerList);
        jsonObject.put("failList", playRoom.getFailList());
        jsonObject.put("type", 3);
        return jsonObject;
    }

    //封装发送给其他玩家某个玩家正在重连接的消息的 JSON 对象
    public static JSONObject buildReConnectingJSONObject(String message) {
        JSONObject textMsg = new JSONObject();
        textMsg.put("message", message);
        textMsg.put("type", 4);
        return textMsg;
    }

    //封装掉线的信息 JSON 对象
    public static JSONObject buildLostConnectJSONObject(String message) {
        JSONObject textMsg = new JSONObject();
        textMsg.put("message", message);
        textMsg.put("type", 5);
        return textMsg;
    }

    //封装加入房间的信息 JSON 对象
    public static JSONObject buildJoinPlayRoomJSONObject(String message) {
        JSONObject textMsg = new JSONObject();
        textMsg.put("message", message);
        textMsg.put("type", 6);
        return textMsg;
    }

    //封装退出房间的信息 JSON 对象
    public static JSONObject buildQuitJSONObject(String message) {
        JSONObject textMsg = new JSONObject();
        textMsg.put("message", message);
        textMsg.put("type", 7);
        return textMsg;
    }

    //封装由于所有玩家游戏结束而导致游戏提前结束信息的 JSON 对象
    public static JSONObject buildGameOverJSONObject(String message) {
        JSONObject textMsg = new JSONObject();
        textMsg.put("message", message);
        textMsg.put("type", 8);
        return textMsg;
    }

    //封装错误信息的 JSON 对象
    public static JSONObject buildErrorJSONObject(String message) {
        JSONObject textMsg = new JSONObject();
        textMsg.put("message", message);
        textMsg.put("type", 9);
        return textMsg;
    }
}
