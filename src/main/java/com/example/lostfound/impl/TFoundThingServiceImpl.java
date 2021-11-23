package com.example.lostfound.impl;

import com.example.lostfound.entity.TFoundThing;
import com.example.lostfound.dao.TFoundThingMapper;
import com.example.lostfound.entity.vo.TFoundThingVO;
import com.example.lostfound.service.TFoundThingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@Service
public class TFoundThingServiceImpl extends ServiceImpl<TFoundThingMapper, TFoundThing> implements TFoundThingService {

    @Override
    public List<TFoundThingVO> converToLossVO(List<TFoundThing> list) {
        final List<TFoundThingVO> thingVOList = list.stream().map(e -> {
            final TFoundThingVO tFoundThingVO = new TFoundThingVO();
            BeanUtils.copyProperties(e, tFoundThingVO);
            return tFoundThingVO;
        }).collect(Collectors.toList());
        return thingVOList;
    }
}
