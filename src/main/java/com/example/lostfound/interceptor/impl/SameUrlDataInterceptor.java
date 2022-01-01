package com.example.lostfound.interceptor.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.lostfound.annotation.RepeatSubmit;
import com.example.lostfound.constant.Constants;
import com.example.lostfound.interceptor.RepeatSubmitInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Zero
 * @date 2021/12/26 12:53
 * @description
 * @since 1.8
 **/
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {

    private final String REPEAT_PARAMS = "repeatParams";

    private final String REPEAT_TIME = "repeatTime";

    private static final Logger LOGGER = LoggerFactory.getLogger(SameUrlDataInterceptor.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 通过redis的字符串结构实现
     * 首先，我们获取请求中的参数，参数分两种：一种是body，也就是post请求，一种是get请求也就是url（parameter）；然后将当前时间戳和参数放入map中
     * 获取请求的url，以url为key，参数和时间戳map为value，再次封装为一个map
     * 通过请求中的请求头中的token作为redis的key，如果redis中存在，则获取出来，然后查看是否是当前url，如果是则对比参数是否一样，时间差是否在预设范围内，如果
     * 在则认为是重复请求，否则就将这个数据保存到redis，redis数据页设置了过期时间为预设的时间
     * @param request 请求
     * @param annotation 防重复提交注解
     * @return
     */
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) {
        String nowParams = "";

        //获取请求体中的参数
        nowParams = getBodyString(request);

        // body参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams)) {
            nowParams = JSONObject.toJSONString(request.getParameterMap());
        }
        Map<String, Object> nowDataMap = new HashMap<String, Object>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        // 请求地址（作为存放cache的key值）
        String submitKey = request.getRequestURI();

        // 唯一标识（指定key + 消息头）
        String cacheRepeatKey = Constants.REPEAT_SUBMIT_KEY + submitKey;

        Object sessionObj = redisTemplate.opsForValue().get(cacheRepeatKey);
        //已经提交请求
        if (sessionObj != null) {
            Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
            //包含了当前的url
            if (sessionMap.containsKey(submitKey)) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(submitKey);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, annotation.interval())) {
                    return true;
                }
            }
        }
        Map<String, Object> cacheMap = new HashMap<String, Object>();
        cacheMap.put(submitKey, nowDataMap);
        redisTemplate.opsForValue().set(cacheRepeatKey, cacheMap, annotation.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    /**
     * 获取请求体中的参数
     */
    private String getBodyString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try(InputStream inputStream = request.getInputStream()) {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("获取请求体中参数错误！");
        } finally {
          if(reader != null) {
              try {
                  reader.close();
              } catch (IOException e) {
                  LOGGER.error(e.getMessage());
              }
          }
        }
        return sb.toString();
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        if ((time1 - time2) < interval)
        {
            return true;
        }
        return false;
    }

}
