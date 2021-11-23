package com.example.lostfound.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.dao.TUserStarMapper;
import com.example.lostfound.entity.UserStar;
import com.example.lostfound.service.TUserStarService;
import org.springframework.stereotype.Service;

/**
 * @author Zero
 * @date 2021/11/23 23:49
 * @description
 * @since 1.8
 **/
@Service
public class TUserStarServiceImpl extends ServiceImpl<TUserStarMapper, UserStar> implements TUserStarService {
}
