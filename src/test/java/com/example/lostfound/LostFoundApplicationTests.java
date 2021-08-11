package com.example.lostfound;

import cn.hutool.crypto.SecureUtil;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.utils.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.Date;

@SpringBootTest
class LostFoundApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testRedis() throws IOException {
//        redisUtil.set("test", "test");
        System.out.println(redisUtil.get("test"));
//        redisUtil.set("key", new TUser(){{setId(1);setNickeName("test");setClazz("texd");setCollage("fjieo");}});
//        System.out.println(redisUtil.get("key"));
        final String key = redisUtil.get("key").toString();

//        final TUser tUser = new ObjectMapper().readValue(key, TUser.class);

    }
    @Test
    void te() throws IOException {
        final long time = new Date().getTime();
        String username="test";
        String password= "chl252599";
        String data = "usernm=" + username + "pwd=" + password + "timesp=" + time;
        String md5 = SecureUtil.md5(data);
        System.out.println(time);
        System.out.println(md5);


    }
}
