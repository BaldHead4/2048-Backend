package com.ecnu.websocketDemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.websocketDemo.Utils.WebSocketUtil;
import org.omg.CORBA.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Component
public class PlayerController {

    public JSONObject reconnect(JSONObject jsonObject) {
        System.out.println("player reconnect: success");
        System.out.println(jsonObject.toString());
        //WebSocketConnect server = new WebSocketConnect();
        return null;
    }

    /*
    * 前端 onOpen 之后，通过该接口传递 json 数据 id, username, difficulty
    * 判断是否已经在难度队列中，
    * 1. 如果不在难度队列中则添加到相应的难度队列中
    * 如果在难度队列中则说明已经开始游戏，需要重连
    * */
    public JSONObject openConnection(String message) {
        JSONObject jsonObject = JSON.parseObject(message);

        //System.out.println("onConnection: success");
        //System.out.println(jsonObject.toString());

        Integer difficulty = (Integer) jsonObject.get("difficulty");
        String id = (String) jsonObject.get("id");
        String username = (String) jsonObject.get("username");
        WebSocketConnect connect = WebSocketUtil.webSocketSet.get(id);

        //System.out.println(id + " " + username + " " + difficulty);
        //System.out.println("onConnect: " + WebSocketUtil.webSocketSet);
        //System.out.println(WebSocketUtil.webSocketSet.get(id));

        if (WebSocketUtil.webSocketSet.get(id) != null) {
            if (WebSocketUtil.userIdForEasy.contains(id) || WebSocketUtil.userIdForHard.contains(id)) {
                //reconnect();
            } else {
                if (difficulty == 1 ) {
                    if (WebSocketUtil.userIdForEasy.size() >= WebSocketUtil.MAX_PLAYER) {
                        WebSocketUtil.AppointSending(id, "简单难度玩家已满，请重新匹配或重新选择难度");
                    } else {
                        WebSocketUtil.userIdForEasy.add(id);
                        System.out.println("EasyList: success");
                    }
                }else if (difficulty == 2){
                    if (WebSocketUtil.userIdForHard.size() >= WebSocketUtil.MAX_PLAYER) {
                        WebSocketUtil.AppointSending(id, "困难难度玩家已满，请重新匹配或重新选择难度");
                    } else {
                        WebSocketUtil.userIdForHard.add(id);
                        System.out.println("HardList: success");
                    }
                }
            }
        }

        return null;
    }

}
