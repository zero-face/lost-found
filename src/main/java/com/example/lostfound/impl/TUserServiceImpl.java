package com.example.lostfound.impl;

import com.example.lostfound.entity.TUser;
import com.example.lostfound.mapper.TUserMapper;
import com.example.lostfound.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

}
