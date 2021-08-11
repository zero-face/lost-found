package com.example.lostfound.validate.code;

import com.alibaba.fastjson.JSON;
import com.example.lostfound.config.handler.CustomizeAuthenticationFailureHandler;
import com.example.lostfound.constant.RedisCode;
import com.example.lostfound.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
    private RedisUtil redisUtil;
    @Autowired
    //自定义验证失败处理器
    private CustomizeAuthenticationFailureHandler customizeAuthenticationFailureHandler;

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
        }
        filterChain.doFilter(request, response);
    }
    private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException, ValidateCodeException {
        final ImageCode codeInRedis = (ImageCode)redisUtil.get(RedisCode.IMAGE_CODE.getMsg());
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空 ");
        }
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码不存在！");
        }
        if (codeInRedis.isExpire()) {
            redisUtil.delete((String) redisUtil.get(RedisCode.IMAGE_CODE.getMsg()));
            throw new ValidateCodeException("验证码已过期！");
        }
        if (!StringUtils.equalsIgnoreCase(codeInRedis.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        redisUtil.delete((String) redisUtil.get(RedisCode.IMAGE_CODE.getMsg()));
    }

}
