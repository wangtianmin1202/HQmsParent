package com.hand.custom.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author tainmin.wang
 * @version date：2019年9月26日 下午4:52:23
 * 
 */
@Component
public class EchoWebSocketHandler implements WebSocketHandler {

	private static List<WebSocketSession> inners = new ArrayList<WebSocketSession>();
	private static List<EchoWebSocketHandler> echos = new ArrayList<EchoWebSocketHandler>();

	private EchoWebSocketHandler() {
		
	}

	public static EchoWebSocketHandler getObject() {
		EchoWebSocketHandler echo = new EchoWebSocketHandler();
		echos.add(echo);
		return echo;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		inners.add(session);
		session.sendMessage(new TextMessage("成功了"));
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean sendMessage(String mes) throws IOException {
		inners.forEach(inner->{
			try {
				inner.sendMessage(new TextMessage(mes));
			} catch (IOException e) {
			}
		});
		return true;
	}
}
