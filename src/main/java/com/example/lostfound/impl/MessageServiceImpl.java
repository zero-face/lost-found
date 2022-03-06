package com.example.lostfound.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.dao.MessageMapper;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.entity.vo.MesVO;
import com.example.lostfound.entity.TMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private TUserService userService;

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

}
