package com.example.lostfound.aspectj;

/**
 * @author Zero
 * @date 2021/12/26 23:49
 * @description
 * @since 1.8
 **/
public class test {
    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 10; i++) {
            System.out.println(System.currentTimeMillis());
            Thread.sleep(1000);
        }
    }
}
