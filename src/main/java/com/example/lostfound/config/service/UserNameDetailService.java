package com.example.lostfound.config.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * @Author Zero
 * @Date 2021/6/3 22:58
 * @Since 1.8
 **/
@Service("userdetailservice")
public class UserNameDetailService implements UserDetailsService {


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(null == username|| "".equals(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        //这里通过查找获取权限
//        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(s1);

//        return new User(user.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()), user.getEnabled(),user.getAccountNotExpired(),user.getCredentialsNotExpired(),user.getAccountNotLocked(),auths);
        return null;
    }
}
