package com.example.lostfound.constant;

/**
 * @Author Zero
 * @Date 2021/8/11 4:49
 * @Since 1.8
 * @Description
 **/
public enum RedisCode {
    IMAGE_CODE("REDIS_KEY_IMAGE_CODE"),
    MAIL_CODE("REDIS_KEY_MAIL_CODE");
    private String msg;

    RedisCode(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
