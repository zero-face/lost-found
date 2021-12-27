package com.example.lostfound.annotation;

import com.example.lostfound.enums.BusinessType;
import com.example.lostfound.enums.OperatorType;

import java.lang.annotation.*;

/**
 * @author Zero
 * @date 2021/12/25 23:00
 * @description 自定义日志注解记录操作日志
 * @since 1.8
 **/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块
     */
    String title() default "";

    /**
     * 功能 操作的功能
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类型(默认后台用户)
     */
    OperatorType operatorType() default OperatorType.ADMIN;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;

}
