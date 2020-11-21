package com.ecnu.websocketDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebsocketDemoApplicationTests {

    @Test
    void contextLoads() {
        String json2048 = "{\n" +
                "  \"score\": 1024,\n" +
                "  \"blocks\": [\n" +
                "    {\n" +
                "      \"position\": {\n" +
                "        \"x\": 1,\n" +
                "        \"y\": 2\n" +
                "      },\n" +
                "      \"merged\": false,\n" +
                "      \"status\": 8,\n" +
                "      \"id\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"position\": {\n" +
                "        \"x\": 1,\n" +
                "        \"y\": 2\n" +
                "      },\n" +
                "      \"merged\": false,\n" +
                "      \"status\": 8,\n" +
                "      \"id\": 2\n" +
                "    }\n" +
                "  ],\n" +
                "  \"mergedBlocks\": [\n" +
                "    {\n" +
                "      \"from\": [0, 1]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"newBlocks\": [\n" +
                "    {\n" +
                "      \"position\": {\n" +
                "        \"x\": 1,\n" +
                "        \"y\": 2\n" +
                "      },\n" +
                "      \"merged\": false,\n" +
                "      \"status\": 16,\n" +
                "      \"id\": 3\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONObject array = JSON.parseObject(json2048);

    }

}
