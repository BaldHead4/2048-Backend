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




}
