package com.example.lostfound.controller;

import com.example.lostfound.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zero
 * @date 2022/6/12 17:27
 * @description
 * @since 1.8
 **/
@RestController
@RequestMapping("/server/trigger")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/test")
    public String test(String name) {
        return testService.test(name);
    }

    @RequestMapping("/bool")
    public boolean bool() {
        return testService.bool();
    }

    @RequestMapping("/test2")
    public String test2() {
        return testService.test2();
    }

    @RequestMapping("/test3")
    public void test3() {
        testService.test3();
    }

    @RequestMapping("/test4")
    public void test4() {
        testService.test4();
    }

    @RequestMapping("/test5")
    public void test5() {
        testService.test5();
    }

}
