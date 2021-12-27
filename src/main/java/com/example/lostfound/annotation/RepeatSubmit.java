package com.example.lostfound.annotation;

import java.lang.annotation.*;

/**
 * @author Zero
 * @date 2021/12/25 22:42
 * @description 自定义注解防止表单重复提交
 * @since 1.8
 **/
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 间隔时间ms，小于此时间的视为重复提交
     */
    int interval() default 5000;

    /**
     * 重复提交的提示消息
     */
    String message() default "不允许重复提交，请稍后重试";
}
