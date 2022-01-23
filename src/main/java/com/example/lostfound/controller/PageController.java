package com.example.lostfound.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Zero
 * @date 2022/1/12 17:37
 * @description
 * @since 1.8
 **/
@Controller
public class PageController {

    @GetMapping("/show")
    public String test() {
        System.out.println("test");
        return "/show.html";
    }
}
