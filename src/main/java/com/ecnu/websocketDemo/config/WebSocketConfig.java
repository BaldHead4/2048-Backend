package com.ecnu.websocketDemo.config;

import com.ecnu.websocketDemo.controller.PlayerController;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import sun.text.UCompactIntArray;

import javax.swing.*;

@Component
@EnableWebSocket
@Configuration
public class WebSocketConfig {

    /**
     * ServerEndpointExporter 作用
     *
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}