package com.example.lostfound.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lostfound.entity.vo.MesVO;
import com.example.lostfound.entity.TMessage;

import java.util.List;

/**
 * @Author Zero
 * @Date 2021/8/14 21:55
 * @Since 1.8
 * @Description
 **/
public interface MessageService extends IService<TMessage> {
    List<MesVO> formMes(List<TMessage> list);
}
