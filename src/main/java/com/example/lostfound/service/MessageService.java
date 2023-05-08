package com.example.lostfound.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lostfound.entity.vo.ChatSessionVO;
import com.example.lostfound.entity.vo.MesVO;
import com.example.lostfound.entity.TMessage;
import com.example.lostfound.entity.vo.NoticesVO;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @Author Zero
 * @Date 2021/8/14 21:55
 * @Since 1.8
 * @Description
 **/
public interface MessageService extends IService<TMessage> {
    List<MesVO> formMes(List<TMessage> list);

    List<ChatSessionVO> getMySessions(Integer userId);

    CompletableFuture<Boolean> persistence(TMessage message);

    CompletableFuture<Boolean> readAllMsg(String sessionId, String too, Integer type);

    List<TMessage> getUnreadMesByUser(Integer id);

    ChatSessionVO getNoticesSession(Integer userId, Integer type);

    List<NoticesVO> convertToNoticesVO(List<TMessage> systemMes, Integer type);
}
