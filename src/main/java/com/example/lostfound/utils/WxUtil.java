package com.example.lostfound.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.entity.wxTemplate.PubNotify;
import com.example.lostfound.entity.wxTemplate.WxEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zero
 * @date 2023/3/19 17:44
 * @description
 * @since 1.8
 **/
@Component
@Slf4j
public class WxUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    private static final String TOKEN_BASE_URL = "https://api.weixin.qq.com/cgi-bin/token";

    private static final String POST_SUB_MES = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";

    @Value("${test}")
    private String test;




    public String getAccessToken() {
        StringBuffer params = new StringBuffer();
        params.append("?grant_type=client_credential&" + "&appid=" + appid +"&secret="+secret);
        final ResponseEntity<String> exchange = restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, URI.create(TOKEN_BASE_URL
                + params)), String.class);
        if(exchange.getStatusCodeValue() == 200) {
            log.info(exchange.getBody());
            final JSONObject jsonObject = JSON.parseObject(exchange.getBody());
            final String accessToken = jsonObject.getString("access_token");
            return accessToken;
        } else if(exchange.getStatusCode().value() == 40029){
            throw new BusinessException(EmBusinessError.PRIMARY_ERROR,"无效的code");
        }else if(exchange.getStatusCode().value() == 45011){
            throw new BusinessException(EmBusinessError.PRIMARY_ERROR,"登录过于频繁");
        } else {
            throw new BusinessException(EmBusinessError.PRIMARY_ERROR,"系统繁忙");
        }
    }

    public String postSubMes(String token, Map<String, Object> maps) {
        log.info("开始在微信推送消息消息");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?access_token=" + token);
        final HttpEntity httpEntity = new HttpEntity(maps);
        final ResponseEntity<String> exchange = restTemplate.exchange(POST_SUB_MES + stringBuilder, HttpMethod.POST, httpEntity, String.class);
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

    // 发布成功模版：8S3jsiyoDbdXfiXCJ6bd66Xk1j8dzWZ_dNKGPk_N2dI
    // 咨询回复模版：c8ji8CcuH4fCxaVYZnpSl8KR6uPLFyNKZvbnCrKamP8
    // 审核结果通知模版：7q5Qurvb1mHHTURQ6GwwVwYSvKfB7iDwLZdZ92c8R6I
    public Map<String, Object> buildWxMes(String openid, PubNotify entity, String templateId) {
        Map<String, Object> maps= new HashMap<>();
        maps.put("touser", openid);
        maps.put("template_id", templateId);
        maps.put("page", "pages/community/community");
        maps.put("miniprogram_state", "trial");
        maps.put("data", entity);
        return maps;
    }


}
