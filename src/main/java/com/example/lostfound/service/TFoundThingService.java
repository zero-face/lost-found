package com.example.lostfound.service;

import com.example.lostfound.entity.TFoundThing;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lostfound.entity.TLossThing;
import com.example.lostfound.entity.TMessage;
import com.example.lostfound.entity.vo.TFoundThingVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
public interface TFoundThingService extends IService<TFoundThing> {

    List<TFoundThingVO> converToLossVO(List<TFoundThing> list);

    List<TFoundThing> getLossBySearchAndTime(String search, Integer time, Integer type);

    void pubNotify(TMessage pubLossMes, TFoundThing foundThing);
}
