package com.example.lostfound.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lostfound.vo.MesVO;
import com.example.lostfound.vo.MessageVO;

import java.util.List;

/**
 * @Author Zero
 * @Date 2021/8/14 21:55
 * @Since 1.8
 * @Description
 **/
public interface MessageService extends IService<MessageVO> {
    List<MesVO> formMes(List<MessageVO> list);
}
