package com.example.lostfound.utils;

import org.springframework.util.DigestUtils;
import org.testng.annotations.Test;

import java.util.*;

import static java.util.Arrays.binarySearch;

/**
 * @author Zero
 * @date 2023/3/18 23:59
 * @description
 * @since 1.8
 **/
public class MesUtil {

    public static String generateSessionId(String from, String to) {
        String[] sessionArr = new String[]{from, to};
        Arrays.sort(sessionArr);
        String key = "";
        for(String s : sessionArr) {
            key += s;
        }
        final String sessionId = DigestUtils.md5DigestAsHex((key).getBytes());
        return sessionId;
    }


    // main
    public static void main(String[] args) {

    }

}
