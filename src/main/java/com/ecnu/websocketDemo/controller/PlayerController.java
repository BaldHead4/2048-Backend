package com.ecnu.websocketDemo.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;

@RestController
public class PlayerController {

    /*
    * 前端 onOpen 之后，通过该接口传递 json 数据 id, username, difficulty，
    *
    *
    * */
    @ResponseBody
    @RequestMapping("/player")
    public JSONObject openConnection(JSONObject jsonObject) {
        System.out.println("player: success");
        System.out.println(jsonObject.toString());
        //WebSocketConnect server = new WebSocketConnect();
        return null;
    }
}
