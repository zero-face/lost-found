package com.example.lostfound.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Zero
 * @Date 2021/8/11 21:50
 * @Since 1.8
 * @Description
 **/
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("time", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("publishTime", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("foundTime", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("lossTime", new Date(), metaObject);
        this.setFieldValByName("lastLoginTime", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("submitTime", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("findTime", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("sendTime", System.currentTimeMillis(), metaObject);
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("lastLoginTime", System.currentTimeMillis(), metaObject);
    }
}
