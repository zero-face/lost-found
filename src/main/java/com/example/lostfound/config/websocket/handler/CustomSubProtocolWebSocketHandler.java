package com.example.lostfound.config.websocket.handler;


import com.alibaba.fastjson.JSON;
import com.example.lostfound.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

/**
 * @author Zero
 * @date 2023/3/19 20:44
 * @description 处理连接断开和建立逻辑
 * @since 1.8
 **/
public class CustomSubProtocolWebSocketHandler extends SubProtocolWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSubProtocolWebSocketHandler.class);

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageService messageService;

    public CustomSubProtocolWebSocketHandler(MessageChannel clientInboundChannel, SubscribableChannel clientOutboundChannel) {
        super(clientInboundChannel, clientOutboundChannel);
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final String uid = session.getPrincipal().getName();
        LOGGER.info("New websocket connection " + uid + " was established");
        // 添加session
        sessionHandler.register(session);
        // 发送未读消息
        String destination = "/exchange/sendToUser/" + uid;
        messageService.getUnreadMesByUser(Integer.valueOf(uid)).forEach(mes -> {
            template.convertAndSend(destination, JSON.toJSONString(mes));
        });
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        LOGGER.info("New websocket connection:"+ session.getPrincipal().getName() + " was closed");
        // 移除session
        sessionHandler.remove(session);
        super.afterConnectionClosed(session,closeStatus);
    }
}
