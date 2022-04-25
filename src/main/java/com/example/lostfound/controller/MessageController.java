//package com.example.lostfound.controller;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.example.lostfound.core.response.CommonReturnType;
//import com.example.lostfound.entity.MessageBody;
//import com.example.lostfound.entity.TUser;
//import com.example.lostfound.service.MessageService;
//import com.example.lostfound.service.TLossThingService;
//import com.example.lostfound.service.TUserService;
//import com.example.lostfound.entity.vo.MesVO;
//import com.example.lostfound.entity.TMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * @Author Zero
// * @Date 2021/8/14 21:54
// * @Since 1.8
// * @Description
// **/
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/mes")
//public class MessageController {
//
//    @Autowired
//    private MessageService messageService;
//    @Autowired
//    private TUserService userService;
//    @Autowired
//    private TLossThingService lossThingService;
//
//    @Autowired
//    private SimpMessageSendingOperations simpMessageSendingOperations;
//
//
//    /**
//     * 根据mesId拿到发送者用户信息
//     * @param mesId
//     * @return
//     */
//    @GetMapping("/{id}")
//    public CommonReturnType getMessageById(@PathVariable("id")String mesId) {
//        final TMessage id = messageService.getOne(new QueryWrapper<TMessage>().eq("id", mesId));
//        if(null == id) {
//            return CommonReturnType.fail(null,"获取失败");
//        }
//        final TUser froms = userService.getOne(new QueryWrapper<TUser>().eq("froms", id.getFroms()));
//        Map<String,Object> maps = new HashMap<>();
//        maps.put("username", froms.getNickName());
//        maps.put("address", froms.getAddressUrl());
//        return CommonReturnType.success(maps,"获取成功");
//    }
//
//    /**
//     * 获取历史消息
//     * @param fromId
//     * @param toId
//     * @return
//     */
//    @GetMapping("/history")
//    public CommonReturnType history(@RequestParam("fromId")Integer fromId,
//                                    @RequestParam("toId")Integer toId) {
//        final List<TMessage> froms = messageService.list(new QueryWrapper<TMessage>()
//                .eq(true, "froms", fromId).eq("too", toId).eq("type","1"));
//        final List<TMessage> too = messageService.list(new QueryWrapper<TMessage>()
//                .eq(true, "froms", toId).eq("too", fromId).eq("type","1"));
//        if ((froms == null || froms.size() < 1) &&(too == null || too.size() < 1)) {
//            return CommonReturnType.fail("没有聊天记录","获取失败");
//        }
//        List<TMessage> list = Stream.of(froms,too)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList())
//                .stream()
//                .sorted(Comparator.comparing(TMessage::getGmtCreate))
//                .collect(Collectors.toList());
//        return CommonReturnType.success(list,"获取成功");
//    }
//
//    /**
//     * 获取离线消息
//     * @param id
//     * @return
//     */
//    @GetMapping("/offline")
//    public CommonReturnType offlineMes(@RequestParam("id")Integer id) {
//        final List<TMessage> status = messageService.list(new QueryWrapper<TMessage>().eq(true, "status", 1).eq("too", id).orderByDesc("send_time"));
//        if(status == null) {
//            return CommonReturnType.fail(null,"没有离线消息");
//        }
//        final List<MesVO> mesVOS = messageService.formMes(status);
//        if(mesVOS == null) {
//            return CommonReturnType.fail(null,"没有离线消息");
//        }
//        return CommonReturnType.success(mesVOS,"获取离线消息成功");
//    }
//}
