package com.example.lostfound.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.utils.NotifyUtil;
import com.example.lostfound.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Zero
 * @Date 2021/8/14 20:03
 * @Since 1.8
 * @Description
 **/
@Slf4j
@Component
@ServerEndpoint("/websocket/{uid}")
public class WebSocket {
    /**
     * 通知工具类
     */
    private static NotifyUtil notifyUtil;

    @Autowired
    public void setNotifyUtil(NotifyUtil notifyUtil) {
        WebSocket.notifyUtil = notifyUtil;
    }

    /**
     * 消息服务
     */
    private static MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        WebSocket.messageService = messageService;
    }

    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的用户名
     */
    private Long userId;

    /**
     * 在线人数
     *
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     *  用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<Long,WebSocket> webSocketSet = new ConcurrentHashMap<>();


    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "uid") Long userId){
        this.session = session;
        this.userId = userId;
        addOnlineCount();
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过userId来区分
        webSocketSet.put(userId,this);
        log.info("[WebSocket] 连接成功，当前连接人数为：={}",getOnlineCount());
    }


    @OnClose
    public void OnClose() {
        webSocketSet.remove(this.userId);
        subOnlineCount();
        log.info("[WebSocket] 退出成功，当前连接人数为：={}",webSocketSet.size());
    }

    @OnMessage
    public void OnMessage(String message){
        final MessageVO messageVO = JSON.parseObject(message, MessageVO.class);
        log.info("[WebSocket] 收到{}发送给{}的消息：{}",messageVO.getFroms(),messageVO.getToo(),messageVO.getMessage());
        //保存message
        messageVO.setId(UUID.randomUUID().toString());
        messageService.save(messageVO);
        //转发message
        if(messageVO.getFlag()) {
            AppointSending(messageVO.getToo(),messageVO.getId(),messageVO.getMessage());
        } else {
            GroupSending(messageVO.getMessage());
        }
    }

    /**
     * 指定发送
     * @param
     * @param message
     */
    public void AppointSending(Long to,String mesId,String message){
        try {
            if(webSocketSet.containsKey(to)) {
                webSocketSet.get(to).session.getBasicRemote().sendText(message);
            } else {
                notifyUtil.publish(message,to.toString(),mesId);//通道设置为用户id，这样每个用户只需订阅自己唯一id的通道
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发
     * @param message
     */
    public void GroupSending(String message){
        for (Long userId : webSocketSet.keySet()){
            try {
                webSocketSet.get(userId).session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前在线用户数
     */
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 在线用户数加1
     */
    public static void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    /**
     * 在线用户数减1
     */
    public static void subOnlineCount() {
        onlineCount.decrementAndGet();
    }


}

