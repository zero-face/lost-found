package com.example.lostfound.controller;


import com.example.lostfound.core.response.CommonReturnType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.service.Tags;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/api/v1/found")
@Api(tags = "寻物接口")
public class TFoundThingController {

    @GetMapping("/pubfound")
    public CommonReturnType publishFound() {
        return null;
    }
}

