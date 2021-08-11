package com.example.lostfound.config.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.mapper.TUserMapper;
import com.example.lostfound.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zero
 * @Date 2021/8/11 5:21
 * @Since 1.8
 * @Description
 **/
@Service
public class MailDetailService implements UserDetailsService {
    @Autowired
    TUserMapper tUserMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(null == username|| "".equals(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        String mail = username.substring(0,username.indexOf("@"));
        final QueryWrapper<TUser> queryWrapper = new QueryWrapper<TUser>().eq("qq",mail );
        final TUser user = tUserMapper.selectOne(queryWrapper);
        if(null == user) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("user");
        return new User(user.getTrueName(), user.getPassword(), true,true,true,true , auths);
    }
}
