package com.example.lostfound.annotation;

import com.example.lostfound.constant.Constants;
import com.example.lostfound.enums.LimitType;

import java.lang.annotation.*;


/**
 * @author Zero
 * @date 2021/12/25 22:47
 * @description 自定义限流注解实现接口限流
 * @since 1.8
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流的key
     */
    String key() default Constants.RATE_LIMIT_KEY;

    /**
     * 限流时间时间（ms）
     */
    int time() default 60000;

    /**
     * 限流次数
     */
    int count() default 200;

    /**
     * 限流类型 全局限流还是根据ip限流或者用户限流
     */
    LimitType limitType() default LimitType.DEFAULT;

}
