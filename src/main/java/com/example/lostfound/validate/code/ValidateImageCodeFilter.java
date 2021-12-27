package com.example.lostfound.validate.code;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.example.lostfound.config.security.handler.CustomizeAuthenticationFailureHandler;
import com.example.lostfound.enums.RedisCode;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Zero
 * @Date 2021/7/2 0:34
 * @Since 1.8
 * @Description 图片验证码过滤器
 **/
@Slf4j
@Component
public class ValidateImageCodeFilter extends OncePerRequestFilter {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private CustomizeAuthenticationFailureHandler customizeAuthenticationFailureHandler;
    @Value("{md5.key}")
    private String key;
    @Autowired
    private SymmetricCrypto symmetricCrypto;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.contains(request.getRequestURI(), "login")
                && StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            try {
                validateCode(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                customizeAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
            try {
                validateParams(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                customizeAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validateParams(ServletWebRequest servletWebRequest) throws ServletRequestBindingException, ValidateCodeException {
        String sign = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "sign");
        String timeStamp = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "timeStamp");
        String username = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "username");
        String password = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "password");
        String data = "usernm=" + username + "pwd=" + password + "timesp=" + timeStamp;
        String md5 = SecureUtil.md5(data);
        if(!StringUtils.equals(md5, sign)) {
            throw new ValidateCodeException("账号或者密码错误");
        }
    }

    private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException, ValidateCodeException {
        String usernameInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "username");
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");
        final ImageDTO codeInRedis = (ImageDTO)  redisTemplate.opsForValue().get(RedisCode.IMAGE_CODE.getMsg() + usernameInRequest);
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空 ");
        }
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码不存在！");
        }
        if (codeInRedis.checkExpire()) {
            redisTemplate.delete((RedisCode.IMAGE_CODE.getMsg()));
            throw new ValidateCodeException("验证码已过期！");
        }
        if (!StringUtils.equalsIgnoreCase(codeInRedis.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        redisTemplate.delete((RedisCode.IMAGE_CODE.getMsg()));
    }
}
