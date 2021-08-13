package com.example.lostfound.config.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.dao.TUserMapper;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author Zero
 * @Date 2021/6/3 22:58
 * @Since 1.8
 **/
@Service("userdetailservice")
public class UserNameDetailService implements UserDetailsService {

    @Resource
    private TUserMapper userMapper;


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(null == username|| "".equals(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        final QueryWrapper<TUser> queryWrapper = new QueryWrapper<TUser>().eq("nick_name",username );
        final TUser user = userMapper.selectOne(queryWrapper);
        if(null == user) {
            throw new BusinessException(EmBusinessError.PRIMARY_ERROR,"用户不存在");
        }
        //这里通过查找获取权限
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("user");

        return new User(user.getNickName(), user.getPassword(), auths);
    }
}
