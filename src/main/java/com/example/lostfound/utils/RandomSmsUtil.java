package com.example.lostfound.utils;

import com.example.lostfound.validate.smscode.SmsCode;
import org.apache.commons.lang.RandomStringUtils;


/**
 * @Author Zero
 * @Date 2021/7/4 15:28
 * @Since 1.8
 * @Description TODO
 **/
public class RandomSmsUtil {

    private final  static int expiredTime = 300;
    public static SmsCode createSMSCode() {
        String smsCode = RandomStringUtils.randomNumeric(4);
        return new SmsCode(smsCode, expiredTime);
    }
}
