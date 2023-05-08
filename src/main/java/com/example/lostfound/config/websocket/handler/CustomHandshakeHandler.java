package com.example.lostfound.config.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * @author Zero
 * @date 2023/3/19 21:13
 * @description
 * @since 1.8
 **/
@Slf4j
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.info("--websocket的http连接握手之后--");
        //设置认证用户
        return (Principal) attributes.get("user");
    }

}
