package com.hand.custom.components;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
* @author tainmin.wang
* @version date：2019年9月26日 下午4:49:28
* 
*/
@Configuration
@EnableWebSocket
public class CustomConfiguration implements WebSocketConfigurer{

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addHandler(echoWebSocketHandler(), "/socket-connect").withSockJS();
	}
	@Bean
  	public WebSocketHandler echoWebSocketHandler() {
  		return EchoWebSocketHandler.getObject();
  	}
}
