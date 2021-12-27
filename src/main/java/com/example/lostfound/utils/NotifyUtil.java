package com.example.lostfound.utils;

import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * @Author Zero
 * @Date 2021/8/14 13:59
 * @Since 1.8
 * @Description
 **/
@Component
@Slf4j
public class NotifyUtil {

    Boolean flag = false;
    public boolean publish (String content,String channel) {
        return false;
    }
    public boolean publish (String content,String channel,String mesId) {

        GoEasy goEasy = new GoEasy("http://rest-hangzhou.goeasy.io",null);
        goEasy.publish(mesId,channel,content,new PublishListener() {
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
