package com.example.lostfound.controller;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TThingType;
import com.example.lostfound.service.TThingTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.smartcardio.CommandAPDU;
import java.util.List;

/**
 * @Author Zero
 * @Date 2021/8/17 17:33
 * @Since 1.8
 * @Description
 **/
@RestController
@Slf4j
@RequestMapping("/api/v1/type")
public class TypeThingController {

    @Autowired
    private TThingTypeService thingTypeService;

    /**
     * 获取所有类型
     * @param
     * @return
     */
    @GetMapping
    public CommonReturnType getType() {
        final List<TThingType> id = thingTypeService.list(new QueryWrapper<TThingType>().isNotNull(true, "id"));
        if(id !=null && id .size() > 0) {
            return CommonReturnType.success(id,"获取成功");
        }
        return CommonReturnType.fail("没有该类型","获取失败");
    }
}
