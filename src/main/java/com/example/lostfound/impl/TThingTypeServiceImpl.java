package com.example.lostfound.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.dao.TThingTypeMapper;
import com.example.lostfound.entity.TThingType;
import com.example.lostfound.service.TThingTypeService;
import org.springframework.stereotype.Service;

/**
 * @Author Zero
 * @Date 2021/8/14 13:02
 * @Since 1.8
 * @Description
 **/
@Service
public class TThingTypeServiceImpl extends ServiceImpl<TThingTypeMapper, TThingType> implements TThingTypeService {
}
