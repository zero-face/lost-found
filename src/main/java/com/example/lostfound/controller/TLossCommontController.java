package com.example.lostfound.controller;


import com.example.lostfound.core.response.CommonReturnType;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/api/v1/lcompoment")
@Api(tags = "失物评论接口")
@Validated
public class TLossCommontController {


    @GetMapping
    public CommonReturnType getCompoment(@RequestParam("com")@NotBlank String comment,
                                         @RequestParam("lossId")Integer lossId){

        return null;
    }
}

