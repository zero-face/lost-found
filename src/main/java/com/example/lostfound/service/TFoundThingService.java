package com.example.lostfound.service;

import com.example.lostfound.entity.TFoundThing;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lostfound.entity.TFoundThingVO;
import com.example.lostfound.entity.TLossThing;
import org.springframework.stereotype.Service;

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
}
