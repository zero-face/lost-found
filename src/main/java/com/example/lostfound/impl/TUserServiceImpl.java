package com.example.lostfound.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.enums.RedisCode;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.dao.TUserMapper;
import com.example.lostfound.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.validate.code.ImageDTO;
import com.example.lostfound.validate.code.ValidateCodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@Service
@Slf4j
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {
    @Autowired
    private TUserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RestTemplate restTemplate;
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
            return true;
        }
        return false;
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

    @Override
    public Boolean checkImageCode(String code,String username) {
        final ImageDTO codeInRedis = (ImageDTO)  redisTemplate.opsForValue().get(RedisCode.IMAGE_CODE.getMsg() + username);
        if (StringUtils.isBlank(code)) {
            throw new ValidateCodeException("验证码不能为空 ");
        }
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码不存在！");
        }
        if (codeInRedis.checkExpire()) {
            redisTemplate.delete((RedisCode.IMAGE_CODE.getMsg()));
            throw new ValidateCodeException("验证码已过期！");
        }
        if (!StringUtils.equalsIgnoreCase(codeInRedis.getCode(), code)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        redisTemplate.delete((RedisCode.IMAGE_CODE.getMsg()));
        return true;
    }

    @Override
    public String getOpenIdByCode(String url, Map<String, String> map) throws BusinessException, IOException {
        log.info("获取openid开始=========》");
        StringBuffer params = new StringBuffer();
        params.append("?appid=" + map.get("appid")+"&secret="+map.get("secret") + "&"+"js_code=" + map.get("code") + "&"
                + "grant_type=authorization_code");
        final ResponseEntity<String> exchange = restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, URI.create(url
                + params)), String.class);
        if(exchange.getStatusCodeValue() == 200) {
            log.info(exchange.getBody());
            final JSONObject jsonObject = JSON.parseObject(exchange.getBody());
            final String code = jsonObject.getString("openid");
            return code;
        } else if(exchange.getStatusCode().value() == 40029){
            throw new BusinessException(EmBusinessError.PRIMARY_ERROR,"无效的code");
        }else if(exchange.getStatusCode().value() == 45011){
            throw new BusinessException(EmBusinessError.PRIMARY_ERROR,"登录过于频繁");
        } else {
            throw new BusinessException(EmBusinessError.PRIMARY_ERROR,"系统繁忙");
        }
    }

    @Override
    public TUser getUserInfoByNameOrId(String username, Integer id) {
        final TUser nick_name = userMapper.selectOne(new QueryWrapper<TUser>().eq(id != null,"id", id).eq(username!=null, "nick_name",username));
        return nick_name;
    }
}
