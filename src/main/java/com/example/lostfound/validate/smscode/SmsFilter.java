package com.example.lostfound.validate.smscode;

import com.alibaba.fastjson.JSON;
import com.example.lostfound.config.handler.CustomizeAuthenticationFailureHandler;
import com.example.lostfound.constant.RedisCode;
import com.example.lostfound.utils.RedisUtil;
import com.example.lostfound.validate.code.ValidateCodeException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Zero
 * @Date 2021/7/4 15:23
 * @Since 1.8
 * @Description TODO
 **/
@Component
public class SmsFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private CustomizeAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.contains(httpServletRequest.getRequestURI(), "mail")
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
                validateCode(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException, IOException {
        String mailCodeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "mailCode");
        String mailInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "mail");

        SmsCode mailCodeInRedis = (SmsCode) redisTemplate.opsForValue().get(RedisCode.MAIL_CODE.getMsg() + mailInRequest);
        if (StringUtils.isBlank(mailCodeInRequest)) {
            throw new ValidateCodeException("验证码不能为空！");
        }
        if (null == mailCodeInRedis) {
            throw new ValidateCodeException("验证码不存在！");
        }
        if (mailCodeInRedis.checkExpire()) {
            redisTemplate.delete(RedisCode.MAIL_CODE.getMsg() + mailInRequest);
            throw new ValidateCodeException("验证码已过期！");
        }
        if (!StringUtils.equalsIgnoreCase(mailCodeInRedis.getCode(), mailCodeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        redisTemplate.delete(RedisCode.MAIL_CODE.getMsg() + mailInRequest);
    }
}
