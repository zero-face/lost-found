package com.example.lostfound.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.mapper.TUserMapper;
import com.example.lostfound.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TUserMapper userMapper;


    /**
     * 查询除了自己之外的用户
     * @param username
     * @return
     */
    @Override
    public Boolean checkUsername(String username) {
        if(null == username) {
            return false;
        }
        final TUser nick_name = userMapper.selectOne(new QueryWrapper<TUser>().eq( "nick_name", username));
        if(null == nick_name) {
            return false;
        }
        return true;
    }

    @Override
    public Integer getUserIdByUserName(String username) {
        if(null == username) {
            return 0;
        }
        final TUser tUser = userMapper.selectOne(new QueryWrapper<TUser>().select("id").eq("nick_name", username));
        if(tUser != null) {
            return tUser.getId();
        }
        return 0;

    }

    @Override
    public Boolean modifyUserInfo(TUser user,String oldUsername) {
        if(user == null || oldUsername == null) {
            return false;
        }
        final Integer id = getUserIdByUserName(user.getNickName());
        if(id > 0) {
            return false;
        }
        final Integer oldUser = getUserIdByUserName(oldUsername);
        final int update = userMapper.update(user, new QueryWrapper<TUser>().eq("id", oldUser));
        if(update == 1) {
            return true;
        }
        return false;
    }
}
