package com.example.lostfound.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.service.TFoundLossService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.vo.MesVO;
import com.example.lostfound.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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


    /**
     * 根据mesId拿到发送者用户信息
     * @param mesId
     * @return
     */
    @GetMapping("/{id}")
    public CommonReturnType getMessageById(@PathVariable("id")String mesId) {
        final MessageVO id = messageService.getOne(new QueryWrapper<MessageVO>().eq("id", mesId));
        if(null == id) {
            return CommonReturnType.fail(null,"获取失败");
        }
        final TUser froms = userService.getOne(new QueryWrapper<TUser>().eq("froms", id.getFroms()));
        Map<String,Object> maps = new HashMap<>();
        maps.put("username", froms.getNickName());
        maps.put("address", froms.getAddressUrl());
        return CommonReturnType.success(maps,"获取成功");
    }

    @GetMapping("/history")
    public CommonReturnType history(@RequestParam("fromId")Integer fromId,
                                    @RequestParam("toId")Integer toId) {
        final List<MessageVO> froms = messageService.list(new QueryWrapper<MessageVO>()
                .eq(true, "froms", fromId).eq("too", toId).eq("type","1"));
        final List<MessageVO> too = messageService.list(new QueryWrapper<MessageVO>()
                .eq(true, "froms", toId).eq("too", fromId).eq("type","1"));
        if ((froms == null || froms.size() < 1) &&(too == null || too.size() < 1)) {
            return CommonReturnType.fail("没有聊天记录","获取失败");
        }
        List<MessageVO> list = Stream.of(froms,too)
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparing(MessageVO::getSendTime))
                .collect(Collectors.toList());
        return CommonReturnType.success(list,"获取成功");
    }

    @GetMapping("/offline")
    public CommonReturnType offlineMes(@RequestParam("id")Integer id) {
        final List<MessageVO> status = messageService.list(new QueryWrapper<MessageVO>().eq(true, "status", 1).eq("too", id).orderByDesc("send_time"));
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
