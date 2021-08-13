package com.example.lostfound.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.dao.TLossThingMapper;
import com.example.lostfound.entity.LossDetailVO;
import com.example.lostfound.entity.LossThingVO;
import com.example.lostfound.entity.TLossThing;

import com.example.lostfound.service.TLossThingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
public class TLossThingServiceImpl extends ServiceImpl<TLossThingMapper, TLossThing> implements TLossThingService {

    @Autowired
    private TLossThingMapper lossThingMapper;

    @Override
    @CachePut(value = "redisCache",key = "'RedisLose' + #search")
    public List<TLossThing> getLossBySearchAndTime(String search, Integer time) {
        final List<TLossThing> tLossThings = lossThingMapper.getLossBySearchAndTime(search, time);
        return tLossThings;
    }

    @Override
    public List<LossThingVO> converToLossVO(List<TLossThing> list) {
        final List<LossThingVO> collect = list.stream().map(vo -> {
            final LossThingVO lossThingVO = new LossThingVO();
            BeanUtils.copyProperties(vo, lossThingVO);
            return lossThingVO;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public LossDetailVO converTOLossDetailVO(TLossThing loss) {
        if(loss == null) {
            return null;
        }
        final LossDetailVO lossDetailVO = new LossDetailVO();
        BeanUtils.copyProperties(loss,lossDetailVO);
        return lossDetailVO;
    }
}
