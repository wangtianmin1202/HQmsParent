package com.hand.spc.utils.components;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class MyWebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(), "/mytest-websocket")
                .setAllowedOrigins("*");
        registry.addHandler(new MyWebSocketHandler(), "/mytest-sock-js")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
