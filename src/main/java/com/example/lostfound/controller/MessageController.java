package com.example.lostfound.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * 根据mesId拿到用户信息
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
}
