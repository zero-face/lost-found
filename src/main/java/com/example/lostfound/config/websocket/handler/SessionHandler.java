package com.example.lostfound.config.websocket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zero
 * @date 2023/3/19 20:46
 * @description
 * @since 1.8
 **/
@Component
public class SessionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionHandler.class);
    //   private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    public SessionHandler() {

    }

    public void register(WebSocketSession session) {
        sessionMap.put(session.getPrincipal().getName(), session);
    }

    public void remove(WebSocketSession session) {
        sessionMap.remove(session.getPrincipal().getName());
    }

    public boolean isOnline(String userId){
        return sessionMap.get(userId)!=null;
    }

}
