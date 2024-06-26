package com.example.lostfound.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.config.websocket.handler.SessionHandler;
import com.example.lostfound.entity.TMessage;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.entity.wxTemplate.PubNotify;
import com.example.lostfound.entity.wxTemplate.WxEntity;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.MesUtil;
import com.example.lostfound.utils.WxUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@RestController
@Log4j2
public class WebsocketController {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageService messageService;

    @Autowired
    private TUserService userService;

    @MessageMapping("/sendToAll")
    public String sendToAll(String msg) {
        return msg;
    }

    @MessageMapping("/send")
    @SendTo("/topic")
    public String say(String msg) {
        return msg;
    }

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private WxUtil wxUtil;
    private static final String chatTemplateId = "c8ji8CcuH4fCxaVYZnpSl8KR6uPLFyNKZvbnCrKamP8";


    @MessageMapping("/sendToUser")
    public void sendToUserByTemplate(Map<String,String> params) {
        String fromUserId = params.get("froms");
        String toUserId = params.get("too");
        TMessage tMessage = JSON.parseObject(JSON.toJSONString(params), TMessage.class);
        log.info("消息对象" + tMessage);
        final String sessionId = MesUtil.generateSessionId(fromUserId, toUserId);
        tMessage.setChatSessionId(sessionId);
        String destination = "/exchange/sendToUser/" + toUserId;
        if(!sessionHandler.isOnline(toUserId)) {
            log.warn(toUserId + "不在线，开始通知");
            tMessage.setMsgState("0");
            final TUser toUser = userService.getOne(new QueryWrapper<TUser>().eq("id", toUserId));
            final TUser fromUser = userService.getOne(new QueryWrapper<TUser>().eq("id", fromUserId));
            final String openId = toUser.getOpenId();
            final String accessToken = wxUtil.getAccessToken();
            final PubNotify pubNotify = new PubNotify() {{
                setThing2(new WxEntity(tMessage.getSendText()));
                setDate3(new WxEntity(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                setThing4(new WxEntity(fromUser.getNickName()));
            }};
            final Map<String, Object> notifyBody = wxUtil.buildWxMes(openId, pubNotify, chatTemplateId);
            wxUtil.postSubMes(accessToken, notifyBody);
        }
        log.info("开始持久化");
        final CompletableFuture<Boolean> completableFuture = messageService.persistence(tMessage);
        completableFuture.thenAcceptAsync((res) -> {
            if(res) {
                template.convertAndSend(destination, JSON.toJSONString(tMessage, SerializerFeature.WriteDateUseDateFormat));
            } else {
                tMessage.setType("3");
                tMessage.setSendText("消息发送失败");
                template.convertAndSend("/exchange/sendToUser/" + fromUserId, JSON.toJSONString(tMessage));
            }
        }).exceptionally((exception) -> {
            log.error("message persistence error");
            return null;
        });
    }



    @GetMapping("/sendToAllByTemplate")
    @MessageMapping("/sendToAllByTemplate")
    public void sendToAllByTemplate(@RequestParam String msg) {
        template.convertAndSend("/topic", msg);

    }

    @GetMapping("/send")
    public String msgReply(@RequestParam String msg) {
        template.convertAndSend("/topic", msg);
        return msg;
    }
}
