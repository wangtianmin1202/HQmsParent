package com.hand.spc.utils.components;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.hand.spc.job.service.impl.InterfaceService;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.utils.Utils;

public class WebSocketHandler extends AbstractWebSocketHandler {

	private static Map<String, List<WebSocketSession>> sessionMap = new ConcurrentHashMap<>();
    private static Map<String, String> sessionUserRel = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(InterfaceService.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        URI uri = session.getUri();
        if (null != uri) {
            String parameter = uri.getQuery();
            if (StringUtils.isNotEmpty(parameter)) {
                Map<String, String> parameterMap = new HashMap<>();

                String[] parameterList = parameter.split("&");
                for (String s : parameterList) {
                    parameterMap.put((s.split("="))[0], (s.split("="))[1]);
                }
                String entityCode = parameterMap.get("entityCode");
                addSession(entityCode, session);
                logger.info("web socket connect success.entityCode is {}, sessionis {}, SessionMap is {}", entityCode, session);//, JSONObject.toJSONString(sessionMap)  modified by jy 20191018
            }
        }
    }

    private void addSession(String entityCode, WebSocketSession session) {
        sessionUserRel.put(session.getId(), entityCode);
        if (sessionMap.get(entityCode) != null) {
            sessionMap.get(entityCode).add(session);
        } else {
            List<WebSocketSession> ls = new ArrayList<>();
            ls.add(session);
            sessionMap.put(entityCode, ls);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Map<?, ?> mapData = JSON.parseObject(message.getPayload().toString());
        String entity = sessionUserRel.get(session.getId());

        if (entity != null && !mapData.get("entityCode").equals(entity)) {
            sessionMap.get(entity).remove(session);
            addSession(entity, session);
            sessionUserRel.put(session.getId(), (mapData.get("entityCode")).toString());
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        (sessionMap.get(sessionUserRel.get(session.getId()))).remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        (sessionMap.get(sessionUserRel.get(session.getId()))).remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 提供给后台接口进行发送数据到前台
     * 
     * @param entityCode entityCode
     * @throws Exception Exception
     */
    public void sendMessage(String entityCode) throws IOException {
        if (StringUtils.isEmpty(entityCode)) {
            return;
        }

        String[] entityCodes = entityCode.split(",");
        try {
            for (int i = 0; i < entityCodes.length; i++) {
                List<WebSocketSession> ls = sessionMap.get(entityCodes[i]);
                if (!ls.isEmpty()) {
                    for (WebSocketSession l : ls) {
                        l.sendMessage(new TextMessage("query"));
                        logger.info("web socket send message success. WebSocketSession is {}  WebSocketMessage is {}  EntityCode is {}",
                                        l.getId(), new TextMessage("query"), entityCodes[i]);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("sendMessage IOException!{}", e);
            throw e;
        }
    }

    public void sendMessage(String uuid, Map<EntityR, Integer> maps) throws IOException {
        if (MapUtils.isEmpty(maps)) {
            return;
        }

        try {
            for (Map.Entry<EntityR, Integer> map : maps.entrySet()) {
                List<WebSocketSession> ls = sessionMap.get(map.getKey().getEntityCode());
                if (!ls.isEmpty()) {
                    TextMessage textMessage = null;
                    for (WebSocketSession l : ls) {
                        textMessage = new TextMessage("query");
                        l.sendMessage(textMessage);
                        logger.info(Utils.getLog(uuid, "web socket send message success. WebSocketSession is "
                                        + l.getId() + " WebSocketMessage is " + textMessage + " EntityCode is "
                                        + map.getKey().getEntityCode() + " Calc Count is " + map.getValue()));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(Utils.getLog(uuid, "websocket send message error:" + e.getMessage()));
            //throw e; //暂时不抛异常
        }
    }

}
