package com.example.lostfound.enums;

/**
 * @Author Zero
 * @Date 2021/8/11 4:49
 * @Since 1.8
 * @Description
 **/
public enum RedisCode {
    IMAGE_CODE("IMAGE_CODE"),
    MAIL_CODE("MAIL_CODE"),
    RATE_LIMIT_KEY("RATE_LIME");
    private String msg;

    RedisCode(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
                                                                          