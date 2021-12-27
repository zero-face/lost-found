package com.example.lostfound.constant;

/**
 * @author Zero
 * @date 2021/12/25 22:53
 * @description
 * @since 1.8
 **/
public class Constants {
    /**
     * 限流的redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 防止重复提交的redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

}
