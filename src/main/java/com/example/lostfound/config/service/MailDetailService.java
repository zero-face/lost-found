package com.example.lostfound.config.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.dao.TUserMapper;
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
        String qq = username.substring(0,username.indexOf("@"));
        final QueryWrapper<TUser> wrapper = new QueryWrapper<TUser>().eq("qq", qq);
        final TUser one = tUserMapper.selectOne(wrapper);
        TUser tUser = null;
        //没有这个用户就相当于用邮箱注册
        if(null == one) {
            tUser = new TUser(){{
                setNickName(username);
                setPassword(username);
                setQq(qq);
            }};
            try {
                tUserMapper.insert(tUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tUser = one;
        }
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("user");
        return new User(tUser.getTrueName(), tUser.getPassword(), true,true,true,true , auths);
    }
}
