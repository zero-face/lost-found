package com.example.lostfound.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.dao.TFoundLossMapper;
import com.example.lostfound.entity.TFoundLoss;
import com.example.lostfound.service.TFoundLossService;
import org.springframework.stereotype.Service;

/**
 * @Author Zero
 * @Date 2021/8/14 17:31
 * @Since 1.8
 * @Description
 **/
@Service
public class TFoundLossServiceImpl extends ServiceImpl<TFoundLossMapper, TFoundLoss> implements TFoundLossService {
}
