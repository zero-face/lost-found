package com.example.lostfound.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.dao.MessageMapper;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.vo.MessageVO;
import org.springframework.stereotype.Service;

/**
 * @Author Zero
 * @Date 2021/8/14 21:56
 * @Since 1.8
 * @Description
 **/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageVO> implements MessageService {
}
