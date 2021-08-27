package com.example.lostfound.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.utils.NotifyUtil;
import com.example.lostfound.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
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
    private Integer userId;

    /**
     * 在线人数
     *
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     *  用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<Integer,WebSocket> webSocketSet = new ConcurrentHashMap<>();


    @OnOpen
    public void OnOpen(Session sessions, @PathParam(value = "uid") Integer userId){
        this.session = sessions;
        this.userId = userId;
        addOnlineCount();
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过userId来区分
        webSocketSet.put(userId,this);
        log.info("[WebSocket] 连接成功，当前连接人数为：={},是{}",getOnlineCount(),webSocketSet.keySet());
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnClose
    public void OnClose() {
        webSocketSet.remove(this.userId);
        subOnlineCount();
        log.info("[WebSocket] 退出成功，当前连接人数为：={},是{}",webSocketSet.size(),webSocketSet.keySet());
    }

    @OnMessage
    public void OnMessage(String message){
        log.info("[WebSocket] 退出成功，当前连接人数为：={},shi{}",webSocketSet.size(),webSocketSet.keySet());
        String data = JSON.parse(message).toString();//去除转义字符
        final MessageVO messageVO = JSON.parseObject(data, MessageVO.class);
        log.info("[WebSocket] 收到{}发送给{}的消息：{}",messageVO.getFroms(),messageVO.getToo(),messageVO.getMessage());
        //保存message
        messageVO.setId(UUID.randomUUID().toString());
        messageVO.setStatus(false); //默认是在线消息
        messageService.save(messageVO);
        //转发message
        if(messageVO.getFlag()) {
            AppointSending(messageVO);
        } else {
            GroupSending(messageVO.getMessage());
        }
    }

    /**
     * 指定发送
     * @param
     * @param
     */
    public void AppointSending(MessageVO messageVO ){
        try {
            System.out.println(webSocketSet.keySet());
            System.out.println("包含"+ messageVO.getToo());
            System.out.println(webSocketSet.containsKey(messageVO.getToo()));
            if(webSocketSet.containsKey(messageVO.getToo())) {
                webSocketSet.get(messageVO.getToo()).session.getBasicRemote().sendText(JSON.toJSONString(messageVO));
            } else {
                //离线则保存这个消息
                messageVO.setStatus(true);
                //更改消息为离线消息
                messageService.update(messageVO,new UpdateWrapper<MessageVO>().eq("id", messageVO.getId()));
                //第三方消息推送服务推送消息
                //通道设置为用户id，这样每个用户只需订阅自己唯一id的通道就能获得所有的通
                notifyUtil.publish(JSON.toJSONString(messageVO),messageVO.getToo().toString(),messageVO.getId());
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
        for (Integer userId : webSocketSet.keySet()){
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

