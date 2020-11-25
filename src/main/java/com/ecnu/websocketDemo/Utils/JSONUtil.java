package com.ecnu.websocketDemo.Utils;

import com.alibaba.fastjson.JSONObject;

public class JSONUtil {

    public static JSONObject buildTextJSONObject(String id, String message) {
        JSONObject textMsg = new JSONObject();
        //封装文字信息的 JSON 对象
        textMsg.put("message", message);
        textMsg.put("id", id);
        textMsg.put("sender", "system");
        return textMsg;
    }

    public static JSONObject buildTextJSONObject(String message) {
        JSONObject textMsg = new JSONObject();
        //封装文字信息的 JSON 对象
        textMsg.put("message", message);
        textMsg.put("sender", "system");
        return textMsg;
    }
}
