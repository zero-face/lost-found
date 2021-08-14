package com.example.lostfound.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zero
 * @Date 2021/8/14 17:50
 * @Since 1.8
 * @Description
 **/
@RestController
@RequestMapping("/api/v1/found")
@Api(tags = "已找回接口")
@Validated
@Slf4j
public class TFoundLossController {
}
