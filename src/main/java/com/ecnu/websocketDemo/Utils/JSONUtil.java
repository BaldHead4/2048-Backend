package com.ecnu.websocketDemo.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.websocketDemo.entity.PlayRoom;

import java.util.Map;

public class JSONUtil {

    //封装文字信息的 JSON 对象
    public static JSONObject buildTextJSONObject(String message) {
        JSONObject textMsg = new JSONObject();

        textMsg.put("message", message);
        textMsg.put("type", 0);
        return textMsg;
    }



    /*封装时间到的 JSON 对象*/
    public static JSONObject buildTimeOutJSONObject(String message) {
        JSONObject textMsg = new JSONObject();
        textMsg.put("message", message);
        textMsg.put("type", 2);
        return textMsg;
    }

    /*封装重连时的发送的游戏对局状态 JSON 对象*/
    public static JSONObject buildReConnectJSONObject(PlayRoom playRoom) {
        Map<String, String> gameMessage = playRoom.getGameMessage();
        JSONObject jsonObject = new JSONObject();
        for (String playerId : playRoom.getPlayers()) {
            jsonObject.put(playerId, gameMessage.get(playerId));
        }
        jsonObject.put("type", 3);
        return jsonObject;
    }
}
