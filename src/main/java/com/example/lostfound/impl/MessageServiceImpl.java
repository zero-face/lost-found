package com.example.lostfound.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.dao.MessageMapper;
import com.example.lostfound.entity.TLossThing;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.entity.vo.ChatSessionVO;
import com.example.lostfound.entity.vo.NoticesVO;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.entity.vo.MesVO;
import com.example.lostfound.entity.TMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Zero
 * @Date 2021/8/14 21:56
 * @Since 1.8
 * @Description
 **/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, TMessage> implements MessageService {


    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private TUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TLossThingService lossThingService;

    @Override
    public List<MesVO> formMes(List<TMessage> list) {
        if(list == null || list.size() < 1) {
            return null;
        }
        final List<MesVO> mesVOS = list.stream().map(m -> {
            final MesVO mesVO = new MesVO();
            final TUser id = userService.getUserInfoByNameOrId(null, m.getFroms());
            BeanUtils.copyProperties(id, mesVO);
            BeanUtils.copyProperties(m, mesVO);
            return mesVO;
        }).collect(Collectors.toList());
        return mesVOS;
    }



    @Override
    public List<ChatSessionVO> getMySessions(Integer userId) {
        final List<String> sessionList = messageMapper.allMySession(userId);

        final List<ChatSessionVO> chatSessionVo = sessionList.stream().map((i) -> {
            ChatSessionVO chatSessionVO = new ChatSessionVO();
            final Integer unread = messageMapper.selectCount(new QueryWrapper<TMessage>().eq("chat_session_id", i)
                    .eq("msg_state", 0).eq("type", 1).eq("too", userId));
            final TMessage lastChat = messageMapper.selectOne(new QueryWrapper<TMessage>()
                    .eq("chat_session_id", i)
                    .eq("type", 1)
                    .orderBy(true, false, "gmt_create").last("limit 1"));
            if (lastChat.getFroms() == userId) {
                chatSessionVO.setChatter(lastChat.getToo());
            } else {
                chatSessionVO.setChatter(lastChat.getFroms());
            }
            String mes = lastChat.getSendText();
            if(mes.startsWith("{")) {
                mes = "不支持的消息类型";
            }
            final TUser userInfoByNameOrId = userService.getUserInfoByNameOrId(null, chatSessionVO.getChatter());
            chatSessionVO.setType(lastChat.getType());
            chatSessionVO.setMes(mes);
            chatSessionVO.setChatSessionId(i);
            chatSessionVO.setMesId(lastChat.getId());
            chatSessionVO.setTime(lastChat.getGmtCreate());
            chatSessionVO.setPicUrl(userInfoByNameOrId.getAddressUrl());
            chatSessionVO.setUserName(userInfoByNameOrId.getNickName());
            chatSessionVO.setUnRead(unread);
            return chatSessionVO;
        }).sorted((o1, o2) ->  o2.getTime().compareTo(o1.getTime())).collect(Collectors.toList());
        return chatSessionVo;
    }

    @Override
    // 异步执行持久化
    public CompletableFuture<Boolean> persistence(TMessage message) {
        final CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                final int insert = messageMapper.insert(message);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        });
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> readAllMsg(String sessionId, String too, Integer type) {
        final CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
            final TMessage tMessage = new TMessage() {{
                setMsgState("1");
            }};
            if(type != 1){
                final int update = messageMapper.update(tMessage, new QueryWrapper<TMessage>()
                        .eq("msg_state", 0).eq("type", type).eq("too", too));
                return true;
            }
            final int update = messageMapper.update(tMessage, new QueryWrapper<TMessage>().eq("chat_session_id", sessionId)
                    .eq("msg_state", 0).eq("type", 1).eq("too", Integer.valueOf(too)));
            return true;
        });
        return completableFuture;
    }

    @Override
    public List<TMessage> getUnreadMesByUser(Integer id) {
        final QueryWrapper<TMessage> queryWrapper = new QueryWrapper<TMessage>().eq("too", id).eq("msg_state", 0);
        final List<TMessage> tMessages = messageMapper.selectList(queryWrapper);
        return tMessages;
    }

    @Override
    public ChatSessionVO getNoticesSession(Integer userId, Integer type) {
        ChatSessionVO chatSessionVO = new ChatSessionVO();
        final Integer unRead = messageMapper.selectCount(new QueryWrapper<TMessage>()
                .eq("msg_state", 0).eq("type", type).eq("too", userId));
        final TMessage lastChat = messageMapper.selectOne(new QueryWrapper<TMessage>()
                .eq("type", type).eq("too", userId).orderBy(true, false, "gmt_create").last("limit 1"));
        if(lastChat == null) {
            return chatSessionVO;
        }
        chatSessionVO.setChatter(lastChat.getFroms());
        final TUser userInfoByNameOrId = userService.getUserInfoByNameOrId(null, chatSessionVO.getChatter());
        chatSessionVO.setType(lastChat.getType());
        chatSessionVO.setMes(lastChat.getSendText());
        chatSessionVO.setChatSessionId(lastChat.getChatSessionId());
        chatSessionVO.setMesId(lastChat.getId());
        chatSessionVO.setTime(lastChat.getGmtCreate());
        chatSessionVO.setPicUrl(userInfoByNameOrId.getAddressUrl());
        chatSessionVO.setUserName(userInfoByNameOrId.getNickName());
        chatSessionVO.setUnRead(unRead);
        return chatSessionVO;
    }

    @Override
    public List<NoticesVO> convertToNoticesVO(List<TMessage> systemMes, Integer type) {
        if(systemMes ==null || systemMes.size() == 0) {
            return null;
        }
        final List<NoticesVO> collect = systemMes.stream().map((i) -> {
            final NoticesVO noticesVO = new NoticesVO();
            final TUser byId = userService.getUserInfoByNameOrId(null, i.getFroms());
            final TLossThing id = lossThingService.getOne(new QueryWrapper<TLossThing>().eq("id", i.getLossId()));
            noticesVO.setLossId(i.getLossId());
            if(type != 0) {
                noticesVO.setLossUrl(id.getPictureUrl());
            }

            noticesVO.setUserName(byId.getNickName());
            noticesVO.setFroms(i.getFroms());
            noticesVO.setToo(i.getToo());
            noticesVO.setMes(i.getSendText());
            noticesVO.setMesId(i.getId());
            noticesVO.setPicUrl(byId.getAddressUrl());
            noticesVO.setType(i.getType());
            noticesVO.setTime(i.getGmtCreate());
            return noticesVO;
        }).collect(Collectors.toList());
        return collect;
    }


}
