package com.example.lostfound.enums;

/**
 * @author Zero
 * @date 2021/12/25 22:57
 * @description
 * @since 1.8
 **/
public enum LimitType {

    /**
     * 默认使用全局限流
     */
    DEFAULT,

    /**
     * 根据ip进行限流
     */
    IP,

    /**
     * 根据用户进行限流
     */
    USER
}
