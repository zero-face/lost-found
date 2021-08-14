package com.example.lostfound.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.AuditVO;
import com.example.lostfound.entity.TLossThing;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.temporal.ValueRange;

/**
 * @Author Zero
 * @Date 2021/8/14 13:59
 * @Since 1.8
 * @Description
 **/
@Component
@Slf4j
public class NotifyUtil {

    @Value("${goeasy.key}")
    private String key;
    Boolean flag = false;
    public boolean publish (String content,String channel) {
        log.info(key);
        GoEasy goEasy = new GoEasy("http://rest-hangzhou.goeasy.io",key);
        goEasy.publish(channel, content,new PublishListener() {
            @Override
            public void onFailed(GoEasyError error) {
                log.info("消息推送失败：{}", error.getContent());
            }
            @Override
            public void onSuccess() {
                log.info("消息推送成功", content);
                flag = true;
            }
        });
        return flag;
    }

}
