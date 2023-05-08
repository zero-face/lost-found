package com.example.lostfound.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.entity.vo.ChatSessionVO;
import com.example.lostfound.entity.vo.NoticesVO;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.entity.vo.MesVO;
import com.example.lostfound.entity.TMessage;
import com.example.lostfound.utils.MesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Zero
 * @Date 2021/8/14 21:54
 * @Since 1.8
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/api/v1/mes")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private TUserService userService;
    @Autowired
    private TLossThingService lossThingService;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据mesId拿到发送者用户信息
     * @param mesId
     * @return
     */
    @GetMapping("/{id}")
    public CommonReturnType getMessageById(@PathVariable("id")String mesId) {
        final TMessage id = messageService.getOne(new QueryWrapper<TMessage>().eq("id", mesId));
        if(null == id) {
            return CommonReturnType.fail(null,"获取失败");
        }
        final TUser froms = userService.getOne(new QueryWrapper<TUser>().eq("froms", id.getFroms()));
        Map<String,Object> maps = new HashMap<>();
        maps.put("username", froms.getNickName());
        maps.put("address", froms.getAddressUrl());
        return CommonReturnType.success(maps,"获取成功");
    }

    /**
     * 获取历史消息
     * @param sessionId
     * @return
     */
    @GetMapping("/history")
    public CommonReturnType history(@RequestParam("sessionId")String sessionId) {
        final List<TMessage> sessionMes = messageService.list(new QueryWrapper<TMessage>()
                .eq(true, "chat_session_id", sessionId).eq("type","1"));
        if (sessionMes == null || sessionMes.size() < 1) {
            return CommonReturnType.fail("没有聊天记录","获取失败");
        }
        return CommonReturnType.success(sessionMes,"获取成功");
    }

    @GetMapping("/getSessionId")
    public CommonReturnType getSessionByUid(@RequestParam("froms") String froms,
                                            @RequestParam("too") String too) {
        final String sessionId = MesUtil.generateSessionId(froms, too);
        final List<TMessage> chatMes = messageService.list(new QueryWrapper<TMessage>().eq("chat_session_id", sessionId).eq("type", 1));
        if(chatMes.isEmpty()) {
            return CommonReturnType.fail(null, "没有聊天记录");
        }
        final CompletableFuture<Boolean> completableFuture = messageService.readAllMsg(sessionId, froms, 1);
        return CommonReturnType.success(chatMes, "拿到聊天记录");
    }

    @GetMapping("/resetUnRead")
    public CommonReturnType reset(@RequestParam("froms") String froms,
                                  @RequestParam("too") String too) {
        final String sessionId = MesUtil.generateSessionId(froms, too);
        final CompletableFuture<Boolean> completableFuture = messageService.readAllMsg(sessionId, froms, 1);
        return CommonReturnType.success(null);
    }

    @GetMapping("/sessions")
    public CommonReturnType mySessions(@RequestParam("userId") Integer userId) {
        final List<ChatSessionVO> mySessions = messageService.getMySessions(userId);
        return CommonReturnType.success(mySessions);
    }

    /**
     * 获取互动和系统消息的会话
     * @param userId
     * @param type
     * @return
     */
    @GetMapping("/noticesSession")
    public CommonReturnType noticesSession(@RequestParam("userId") Integer userId,
                                           @RequestParam("type") Integer type) {
        final ChatSessionVO Session = messageService.getNoticesSession(userId, type);
        return CommonReturnType.success(Session);
    }

    /**
     * 获取互动和系统消息
     * @param type
     * @param userId
     * @return
     */
    @GetMapping("/notices")
    public CommonReturnType systemMessage(@RequestParam("type") Integer type,
                                          @RequestParam("userId") Integer userId) {
        final List<TMessage> systemMes = messageService.list(new QueryWrapper<TMessage>().eq("type", type).eq("too", userId).orderBy(true, false, "gmt_create"));
        messageService.readAllMsg(null,String.valueOf(userId), type);
        final List<NoticesVO> noticesVOS = messageService.convertToNoticesVO(systemMes, type);
        if (noticesVOS == null || noticesVOS.size() < 1) {
            return CommonReturnType.fail(null, "没有" + (type == 0 ? "系统消息" : "互动消息"));
        }
        return CommonReturnType.success(noticesVOS);
    }




    @GetMapping("/read")
    public CommonReturnType setReadPoint(@RequestParam("id")Integer mesId,
                                         @RequestParam("chatSession") String session,
                                         @RequestParam("userId")Integer userId) {
//        redisTemplate.opsForHash().put(session, userId, mesId);
        return CommonReturnType.success(null, "success");
    }

    /**
     * 获取离线消息
     * @param id
     * @return
     */
    @GetMapping("/offline")
    public CommonReturnType offlineMes(@RequestParam("id")Integer id) {
        final List<TMessage> status = messageService.list(new QueryWrapper<TMessage>().eq(true, "status", 1).eq("too", id).orderByDesc("send_time"));
        if(status == null) {
            return CommonReturnType.fail(null,"没有离线消息");
        }
        final List<MesVO> mesVOS = messageService.formMes(status);
        if(mesVOS == null) {
            return CommonReturnType.fail(null,"没有离线消息");
        }
        return CommonReturnType.success(mesVOS,"获取离线消息成功");
    }
}
